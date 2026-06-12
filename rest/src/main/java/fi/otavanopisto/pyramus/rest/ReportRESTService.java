package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.PyramusConsts;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditFunding;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentFunding;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.rest.annotation.AuthScope;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.permissions.ReportPermissions;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCredit;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCreditReport;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCreditState;
import fi.otavanopisto.pyramus.rest.util.ISO8601Date;

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
@AuthScope(AuthScope.LEGACY)
public class ReportRESTService extends AbstractRESTService {

  @Inject
  private CourseAssessmentDAO courseAssessmentDAO; 
  
  @Inject
  private CreditLinkDAO creditLinkDAO;

  @Inject
  private KoskiController koskiController;
  
  @Inject
  private StudyProgrammeDAO studyProgrammeDAO;
  
  @Inject
  private TransferCreditDAO transferCreditDAO;
  
  @Path("/perusopetus")
  @GET
  @RESTPermit (ReportPermissions.VIEW_PROGRAMMATIC_REPORT)
  public Response listEducationTypes(@QueryParam("linja") String linja, @QueryParam("begin") ISO8601Date begin, @QueryParam("end") ISO8601Date end) {

    if (linja == null || begin == null || begin.getLocalDate() == null || end == null || end.getLocalDate() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String educationTypeCode;
    Date beginDate = DateUtils.toDate(begin.getLocalDate().atStartOfDay());
    Date endDate = DateUtils.toDate(end.getLocalDate().atTime(23, 59, 59));

    if (beginDate.after(endDate)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Collection<StudyProgramme> studyProgrammes;
    if ("apalu".equals(linja)) {
      StudyProgramme sp1 = studyProgrammeDAO.findById(29L);
      StudyProgramme sp2 = studyProgrammeDAO.findById(33L);
      if (sp1 == null || sp2 == null) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      studyProgrammes = Arrays.asList(sp1, sp2);
      educationTypeCode = PyramusConsts.Apa.EDUCATION_TYPE;
    }
    else if ("paanp".equals(linja)) {
      StudyProgramme sp1 = studyProgrammeDAO.findById(7L);
      StudyProgramme sp2 = studyProgrammeDAO.findById(11L);
      if (sp1 == null || sp2 == null) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      studyProgrammes = Arrays.asList(sp1, sp2);
      educationTypeCode = PyramusConsts.Perusopetus.EDUCATION_TYPE;
    }
    else {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    PerusopetusCreditReport report = new PerusopetusCreditReport();
    
    // Listaa arvosanat aikavälillä
    List<CourseAssessment> assessments = courseAssessmentDAO.listByStudyProgrammesAndDates(studyProgrammes, beginDate, endDate);
    
    for (CourseAssessment assessment : assessments) {
      PerusopetusCredit restCredit = restCreditForCourseAssessment(assessment, educationTypeCode);
      
      if (restCredit.getState() != null && restCredit.getState().isAcceptedState()) {
        report.addAcceptedCredit(restCredit);
      }
      else {
        report.addRejectedCredit(restCredit);
      }
    }
    
    // VOS-hyväksiluvut
    
    List<TransferCredit> vosTCs = transferCreditDAO.listByStudyProgrammesAndDatesAndFunding(studyProgrammes, beginDate, endDate, TransferCreditFunding.GOVERNMENT_FUNDING);
    for (TransferCredit transferCredit : vosTCs) {
      PerusopetusCredit restCredit = restCredit(transferCredit, educationTypeCode);
      
      if (restCredit.getState() != null && restCredit.getState().isAcceptedState()) {
        report.getSummary().incrementAcceptedTransferCreditCount();
      }
      else {
        report.getSummary().incrementRejectedTransferCreditCount();
      }
      
      report.addFundedTransferCredit(restCredit);
    }
    
    // Summary
    
    for (PerusopetusCredit credit : report.getAcceptedCredits()) {
      report.getSummary().incrementAcceptedCreditCount();
      
      if (credit.getState() != null) {
        report.getSummary().incrementAcceptedByState(credit.getState());
      }
      
      if (StringUtils.isNotBlank(credit.getCourseLengthSymbol())) {
        report.getSummary().incrementAcceptedByLengthUnit(credit.getCourseLengthSymbol());
      }
    }

    for (PerusopetusCredit credit : report.getRejectedCredits()) {
      report.getSummary().incrementRejectedCreditCount();
      
      if (credit.getState() != null) {
        report.getSummary().incrementRejectedByState(credit.getState());
      }
      
      if (StringUtils.isNotBlank(credit.getCourseLengthSymbol())) {
        report.getSummary().incrementRejectedByLengthUnit(credit.getCourseLengthSymbol());
      }
    }
    
    return Response.ok(report).build();
  }
  
  private PerusopetusCreditState handleCreditList(List<? extends Credit> creditList, boolean creditsAreCourseAssessments, boolean creditsAreLinks, CourseCredit assessment, PerusopetusCreditState state, List<PerusopetusCredit> previousEvaluations) {
    for (Credit matchingAssessment : creditList) {
      // Skippaa jos credit.id on sama (ts sama Credit löytyy listasta)
      if (matchingAssessment.getId().equals(assessment.getId())) {
        continue;
      }
      
      // Skippaa matchingAssessment, jos sillä ei ole arvosanaa tai arvosana ei ole perusopetuksen sallittu arvosana (lähinnä K:t pois)
      String gradeName = matchingAssessment.getGrade() != null ? matchingAssessment.getGrade().getName() : null;
      if (gradeName == null || !PyramusConsts.Perusopetus.ALLOWED_GRADES.contains(gradeName)) {
        continue;
      }

      // Jos matchingAssessment on annettu ennen arviointia, on kyseessä jonkunsortin korotus
      if (matchingAssessment.getDate().before(assessment.getDate())) {
        // Pelkät perustiedot aiemmista suorituksista
        String creditType = matchingAssessment.getCreditType() == CreditType.CourseAssessment ? (creditsAreLinks ? "L-CA" : "CA")
            : matchingAssessment.getCreditType() == CreditType.TransferCredit ? (creditsAreLinks ? "L-TC" : "TC") : null;

        PerusopetusCredit previousEvaluation = new PerusopetusCredit();
        previousEvaluation.setGradeDate(matchingAssessment.getDate());
        previousEvaluation.setGradeName(gradeName);
        previousEvaluation.setType(creditType);
        previousEvaluations.add(previousEvaluation);
        
        // Samasta kurssista saa rahoitusta vain kerran kalenterivuodessa
        boolean sameYear = creditsAreCourseAssessments && assessment.getDate().getYear() == matchingAssessment.getDate().getYear();
        
        if (state == PerusopetusCreditState.ACCEPTED) {
          state = sameYear ? PerusopetusCreditState.RAISED_SAMEYEAR : PerusopetusCreditState.RAISED;
        }
        
        if (Boolean.TRUE.equals(matchingAssessment.getGrade().getPassingGrade())) {
          // Aiempi suoritus on ollut läpäisevä
          state = sameYear ? PerusopetusCreditState.RAISED_SAMEYEAR_PASSING : PerusopetusCreditState.RAISED_PASSING;
        }
      }
    }
    
    return state;
  }
  

  private PerusopetusCredit restCredit(CourseCredit courseCredit, String educationTypeCode) {
    Student student = courseCredit.getStudent();
    Subject subject = courseCredit.getSubject();
    Integer courseNumber = courseCredit.getCourseNumber();
    String studentName = String.format("%s, %s", student.getLastName(), student.getFirstName());
    String creditType = courseCredit.getCreditType() == CreditType.CourseAssessment ? "CA"
        : courseCredit.getCreditType() == CreditType.TransferCredit ? "TC" : null;
    
    PerusopetusCreditState state = PerusopetusCreditState.ACCEPTED;
    boolean otherFunding = student.getFunding() == StudentFunding.OTHER_FUNDING;
    
    String courseCode;
    if (courseCredit.getSubject() != null && StringUtils.isNotBlank(courseCredit.getSubject().getCode())) {
      courseCode = courseCredit.getSubject().getCode();
      if (courseCredit.getCourseNumber() != null) {
        courseCode += courseCredit.getCourseNumber();
      }
    }
    else {
      courseCode = null;
    }
    
    Double courseLength = null;
    String courseLengthSymbol = null;
    if (courseCredit.getCourseLength() != null && courseCredit.getCourseLength().getUnits() != null && courseCredit.getCourseLength().getUnit() != null && StringUtils.isNotBlank(courseCredit.getCourseLength().getUnit().getSymbol())) {
      courseLength = courseCredit.getCourseLength().getUnits();
      courseLengthSymbol = courseCredit.getCourseLength().getUnit().getSymbol();
    }

    boolean mismatchingCurriculum;
    if (CollectionUtils.isNotEmpty(courseCredit.getCurriculums())) {
      if (student.getCurriculum() != null) {
        mismatchingCurriculum = !courseCredit.getCurriculums().stream().anyMatch(courseCurriculum -> courseCurriculum.getId().equals(student.getCurriculum().getId()));
        if (mismatchingCurriculum) {
          state = PerusopetusCreditState.REJECTED_MISMATCHING_CURRICULUM;
        }
      }
      else {
        // This could be communicated in a different manner, but student 
        // having no curriculum is a problem in this context.
        mismatchingCurriculum = true;
        state = PerusopetusCreditState.REJECTED_MISSING_STUDENT_CURRICULUM;
      }
    }
    else {
      // Credit has no curriculums so it is applicable to all curriculums and cannot mismatch
      mismatchingCurriculum = false;
    }

    boolean evaluatedOutsideStudies = student.getStudyStartDate() == null || courseCredit.getDate() == null || courseCredit.getDate().before(student.getStudyStartDate());
    if (!evaluatedOutsideStudies && student.getStudyEndDate() != null) {
      evaluatedOutsideStudies = courseCredit.getDate().after(DateUtils.endOfDay(student.getStudyEndDate()));
    }
    
    boolean koskiFailure = !koskiController.isSuccessfullyUpdated(student.getPerson());

    // 0h-suoritukset poistetaan
    if (courseCredit.getCourseLength() == null || courseCredit.getCourseLength().getUnits() == null || courseCredit.getCourseLength().getUnits() == 0) {
      state = PerusopetusCreditState.REJECTED_COURSELENGTH;
    }
    
    // K-arvosanat (= kaikki ei-sallitut arvosanat) pois
    String gradeName = courseCredit.getGrade() != null ? courseCredit.getGrade().getName() : null;
    if (gradeName == null || !PyramusConsts.Perusopetus.ALLOWED_GRADES.contains(gradeName)) {
      state = PerusopetusCreditState.REJECTED_GRADE;
    }
    
    List<PerusopetusCredit> previousEvaluations = new ArrayList<>();
    
    if ("MUU".equals(subject.getCode()) || (subject.getEducationType() != null && StringUtils.equals(subject.getEducationType().getCode(), educationTypeCode))) {
      // Kyseessä soveltuvan koulutusasteen tai MUU-aineen suoritus, etsitään edeltävät suoritukset

      // Huom. Saman kurssin/modulin suoritukset etsitään tällä hetkellä saman Studentin tiedoista.
      // On mahdollista, että näitä pitäisi etsiä saman henkilön muidenkin opiskeluoikeuksien alta.
      
      // Kurssisuoritukset Studentin perusteella
      List<CourseAssessment> matchingCourseAssessments = courseAssessmentDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
      state = handleCreditList(matchingCourseAssessments, true, false, courseCredit, state, previousEvaluations);

      // Hyväksiluetut Studentin perusteella
      List<TransferCredit> matchingTransferCredits = transferCreditDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
      state = handleCreditList(matchingTransferCredits, false, false, courseCredit, state, previousEvaluations);
      
      // Siirretyt suoritukset Studentin perusteella
      // Siirtosuoritukset käsitellään kuin ne olisivat hyväksilukuja, vaikka siirretty olisikin kurssisuoritus
      List<CreditLink> matchingCreditLinks = creditLinkDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
      List<Credit> matchingCreditLinkCredits = matchingCreditLinks.stream().map(CreditLink::getCredit).toList();
      state = handleCreditList(matchingCreditLinkCredits, false, true, courseCredit, state, previousEvaluations);
    }
    else {
      state = PerusopetusCreditState.REJECTED_EDUCATIONTYPE;
    }
    
    PerusopetusCredit credit = new PerusopetusCredit();
    credit.setAssessorName(courseCredit.getAssessor() != null ? courseCredit.getAssessor().getFullName() : null);
    credit.setCourseCode(courseCode);
    credit.setCourseLength(courseLength);
    credit.setCourseLengthSymbol(courseLengthSymbol);
    credit.setCourseName(courseCredit.getCourseName());
    credit.setGradeName(courseCredit.getGrade() != null ? courseCredit.getGrade().getName() : null);
    credit.setGradeDate(courseCredit.getDate());
    credit.setGradingScaleName((courseCredit.getGrade() != null && courseCredit.getGrade().getGradingScale() != null) ? courseCredit.getGrade().getGradingScale().getName() : null);
    credit.setPersonId(student.getPersonId());
    credit.setSchoolField((student.getSchool() != null && student.getSchool().getField() != null) ? student.getSchool().getField().getName() : null);
    credit.setSchoolName(student.getSchool() != null ? student.getSchool().getName() : null);
    credit.setStudentId(student.getId());
    credit.setStudentName(studentName);
    credit.setStudyProgrammeName(student.getStudyProgramme() != null ? student.getStudyProgramme().getName() : null);
    credit.setMismatchingCurriculum(mismatchingCurriculum);
    credit.setOtherFunding(otherFunding);
    credit.setEvaluatedOutsideStudies(evaluatedOutsideStudies);
    credit.setKoskiFailure(koskiFailure);
    credit.setState(state);
    credit.setType(creditType);
    
    if (previousEvaluations != null) {
      credit.setPreviousEvaluations(previousEvaluations);
    }
    
    return credit;
  }

  private PerusopetusCredit restCreditForCourseAssessment(CourseAssessment courseAssessment, String educationTypeCode) {
    CourseModule courseModule = courseAssessment.getCourseModule();
    CourseBase courseBase = courseModule.getCourse();

    boolean groupCourse;
    if (courseBase instanceof Course && ((Course) courseBase).getTags() != null) {
      Course course = (Course) courseBase;
      groupCourse = course.getTags().stream().anyMatch(tag -> StringUtils.equals(tag.getText(), "ryhmäkurssi"));
    }
    else {
      groupCourse = false;
    }

    PerusopetusCredit restCredit = restCredit(courseAssessment, educationTypeCode);
    restCredit.setCourseId(courseBase.getId());
    restCredit.setGroupCourse(groupCourse);
    return restCredit;
  }

}
