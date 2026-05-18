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
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentFunding;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCredit;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCreditReport;
import fi.otavanopisto.pyramus.rest.model.report.PerusopetusCreditState;
import fi.otavanopisto.pyramus.rest.util.ISO8601Date;

@Path("/report")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
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
  @RESTPermit (CommonPermissions.LIST_EDUCATIONTYPES)
  public Response listEducationTypes(@QueryParam("linja") String linja, @QueryParam("begin") ISO8601Date begin, @QueryParam("end") ISO8601Date end) {

    if (linja == null || begin == null || begin.getLocalDate() == null || end == null || end.getLocalDate() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
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
    }
    else if ("paanp".equals(linja)) {
      StudyProgramme sp1 = studyProgrammeDAO.findById(7L);
      StudyProgramme sp2 = studyProgrammeDAO.findById(11L);
      if (sp1 == null || sp2 == null) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      studyProgrammes = Arrays.asList(sp1, sp2);
    }
    else {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    PerusopetusCreditReport report = new PerusopetusCreditReport();
    
    // Listaa arvosanat aikavälillä
    List<CourseAssessment> assessments = courseAssessmentDAO.listByStudyProgrammesAndDates(studyProgrammes, beginDate, endDate);
    
    for (CourseAssessment assessment : assessments) {
      Student student = assessment.getStudent();
      Subject subject = assessment.getSubject();
      Integer courseNumber = assessment.getCourseNumber();
      
      // 0h-suoritukset poistetaan
      if (assessment.getCourseLength() == null || assessment.getCourseLength().getUnits() == null || assessment.getCourseLength().getUnits() == 0) {
        report.addRejectedCredit(restCredit(assessment, PerusopetusCreditState.REJECTED_COURSELENGTH, null));
        continue;
      }
      
      // K-arvosanat (= kaikki ei-sallitut arvosanat) pois
      String gradeName = assessment.getGrade() != null ? assessment.getGrade().getName() : null;
      if (gradeName == null || !PyramusConsts.Perusopetus.ALLOWED_GRADES.contains(gradeName)) {
        report.addRejectedCredit(restCredit(assessment, PerusopetusCreditState.REJECTED_GRADE, null));
        continue;
      }
      
      if (subject.getEducationType() != null && StringUtils.equals(subject.getEducationType().getCode(), PyramusConsts.EDUCATION_TYPE_PK)) {
        // Kyseessä perusopetuksen suoritus, etsitään edeltävät suoritukset
        
        PerusopetusCreditState state = PerusopetusCreditState.ACCEPTED;
        List<PerusopetusCredit> previousEvaluations = new ArrayList<>();
        
        // Huom. Saman kurssin/modulin suoritukset etsitään tällä hetkellä saman Studentin tiedoista.
        // On mahdollista, että näitä pitäisi etsiä saman henkilön muidenkin opiskeluoikeuksien alta.
        
        // Kurssisuoritukset Studentin perusteella
        List<CourseAssessment> matchingCourseAssessments = courseAssessmentDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
        state = handleCreditList(matchingCourseAssessments, assessment, state, previousEvaluations);

        // Hyväksiluetut Studentin perusteella
        List<TransferCredit> matchingTransferCredits = transferCreditDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
        state = handleCreditList(matchingTransferCredits, assessment, state, previousEvaluations);
        
        // Siirretyt suoritukset Studentin perusteella
        List<CreditLink> matchingCreditLinks = creditLinkDAO.listByStudentAndSubjectAndCourseNumber(student, subject, courseNumber);
        List<Credit> matchingCreditLinkCredits = matchingCreditLinks.stream().map(CreditLink::getCredit).toList();
        state = handleCreditList(matchingCreditLinkCredits, assessment, state, previousEvaluations);
        
        report.addAcceptedCredit(restCredit(assessment, state, previousEvaluations));
      }
      else {
        // Kyseessä joku muu suoritus, esim lukio
        report.addRejectedCredit(restCredit(assessment, PerusopetusCreditState.REJECTED_EDUCATIONTYPE, null));
      }
    }
    
    return Response.ok(report).build();
  }
  
  private PerusopetusCreditState handleCreditList(List<? extends Credit> creditList, CourseAssessment assessment, PerusopetusCreditState state, List<PerusopetusCredit> previousEvaluations) {
    for (Credit matchingAssessment : creditList) {
      // Skippaa jos credit.id on sama (ts sama Credit löytyy listasta)
      if (!matchingAssessment.getId().equals(assessment.getId())) {
        if (matchingAssessment.getDate().before(assessment.getDate())) {
          // matchingAssessment on aiempi arvosana samasta kurssista/modulista kuin assessment

          // Pelkät perustiedot aiemmista suorituksista
          PerusopetusCredit previousEvaluation = new PerusopetusCredit();
          previousEvaluation.setGradeDate(matchingAssessment.getDate());
          previousEvaluation.setGradeName(matchingAssessment.getGrade() != null ? matchingAssessment.getGrade().getName() : null);
          previousEvaluations.add(previousEvaluation);
          
          if (state == PerusopetusCreditState.ACCEPTED) {
            state = PerusopetusCreditState.RAISED;
          }
          
          if (Boolean.TRUE.equals(matchingAssessment.getGrade().getPassingGrade())) {
            // Aiempi suoritus on ollut läpäisevä
            state = PerusopetusCreditState.RAISED_PASSING;
          }
        }
      }
    }
    
    return state;
  }
  
  
  private PerusopetusCredit restCredit(CourseAssessment courseAssessment, PerusopetusCreditState state, List<PerusopetusCredit> previousEvaluations) {
    Student student = courseAssessment.getStudent();
    String studentName = String.format("%s, %s", student.getLastName(), student.getFirstName());
    
    CourseModule courseModule = courseAssessment.getCourseModule();
    CourseBase courseBase = courseModule.getCourse();
    
    boolean otherFunding = student.getFunding() == StudentFunding.OTHER_FUNDING;
    
    String courseCode;
    if (courseAssessment.getSubject() != null && StringUtils.isNotBlank(courseAssessment.getSubject().getCode())) {
      courseCode = courseAssessment.getSubject().getCode();
      if (courseAssessment.getCourseNumber() != null) {
        courseCode += courseAssessment.getCourseNumber();
      }
    }
    else {
      courseCode = null;
    }
    
    boolean groupCourse;
    if (courseBase instanceof Course && ((Course) courseBase).getTags() != null) {
      Course course = (Course) courseBase;
      groupCourse = course.getTags().stream().anyMatch(tag -> StringUtils.equals(tag.getText(), "ryhmäkurssi"));
    }
    else {
      groupCourse = false;
    }

    String courseLength = null;
    if (courseModule.getCourseLength() != null && courseModule.getCourseLength().getUnits() != null && courseModule.getCourseLength().getUnit() != null && StringUtils.isNotBlank(courseModule.getCourseLength().getUnit().getSymbol())) {
      Double units = courseModule.getCourseLength().getUnits();
      
      // Prettify the number
      String len = units == Math.floor(units) ? String.format("%.0f", units) : Double.toString(units);
      
      courseLength = String.format("%s %s", len, courseModule.getCourseLength().getUnit().getSymbol());
    }

    boolean mismatchingCurriculum;
    if (CollectionUtils.isNotEmpty(courseBase.getCurriculums())) {
      if (student.getCurriculum() != null) {
        mismatchingCurriculum = !courseBase.getCurriculums().stream().anyMatch(courseCurriculum -> courseCurriculum.getId().equals(student.getCurriculum().getId()));
      }
      else {
        // This could be communicated in a different manner, but student 
        // having no curriculum is a problem in this context.
        mismatchingCurriculum = true;
      }
    }
    else {
      // Credit has no curriculums so it is applicable to all curriculums and cannot mismatch
      mismatchingCurriculum = false;
    }

    boolean evaluatedOutsideStudies = student.getStudyStartDate() == null || courseAssessment.getDate() == null || courseAssessment.getDate().before(student.getStudyStartDate());
    if (!evaluatedOutsideStudies && student.getStudyEndDate() != null) {
      evaluatedOutsideStudies = courseAssessment.getDate().after(DateUtils.endOfDay(student.getStudyEndDate()));
    }
    
    boolean koskiFailure = !koskiController.isSuccessfullyUpdated(student.getPerson());
    
    PerusopetusCredit credit = new PerusopetusCredit();
    credit.setAssessorName(courseAssessment.getAssessor() != null ? courseAssessment.getAssessor().getFullName() : null);
    credit.setCourseCode(courseCode);
    credit.setCourseLength(courseLength);
    credit.setCourseName(courseBase != null ? courseBase.getName() : null);
    credit.setGradeName(courseAssessment.getGrade() != null ? courseAssessment.getGrade().getName() : null);
    credit.setGradeDate(courseAssessment.getDate());
    credit.setGradingScaleName((courseAssessment.getGrade() != null && courseAssessment.getGrade().getGradingScale() != null) ? courseAssessment.getGrade().getGradingScale().getName() : null);
    credit.setGroupCourse(groupCourse);
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
    
    if (previousEvaluations != null) {
      credit.setPreviousEvaluations(previousEvaluations);
    }
    
    return credit;
  }
}
