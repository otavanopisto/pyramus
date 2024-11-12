package fi.otavanopisto.pyramus.views.grading;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.enterprise.inject.spi.CDI;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.dao.grading.SpokenLanguageExamDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExam;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExamSkillLevel;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.DateUtils;
import fi.otavanopisto.pyramus.framework.PyramusFormViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableFacade;
import fi.otavanopisto.pyramus.util.ixtable.PyramusIxTableRowFacade;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ManageSpokenLanguageExamsViewController extends PyramusFormViewController implements Breadcrumbable {

  @Override
  public void processForm(PageRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    SpokenLanguageExamDAO spokenLanguageExamDAO = DAOFactory.getInstance().getSpokenLanguageExamDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    
    Long studentId = requestContext.getLong("studentId");
    
    Student student = studentDAO.findById(studentId);
    List<CourseAssessment> courseAssessments = courseAssessmentDAO.listByStudent(student);
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();

    Collections.sort(transferCredits, new StringAttributeComparator("getCourseName", true));

    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));
    String jsonCurriculums = new JSONArrayExtractor("name", "id").extractString(curriculums);

    setJsDataVariable(requestContext, "curriculums", jsonCurriculums);

    JSONArray spokenLanguageCreditsJSONArray = new JSONArray();
    for (CourseAssessment courseAssessment : courseAssessments) {
      // Validate here is for checking for the course codes that are applicable, skip all other courses
      if (!validate(student, courseAssessment)) {
        continue;
      }
      
      SpokenLanguageExam spokenLanguageExam = spokenLanguageExamDAO.findBy(courseAssessment);

      String courseName = courseAssessment.getCourseStudent().getCourse().getName();
      if (StringUtils.isNotBlank(courseAssessment.getCourseStudent().getCourse().getNameExtension())) {
        courseName += " (" + courseAssessment.getCourseStudent().getCourse().getNameExtension() + ")";
      }
      
      JSONObject spokenLanguageCreditJSON = new JSONObject();
      spokenLanguageCreditJSON.put("creditId", courseAssessment.getId());
      spokenLanguageCreditJSON.put("courseName", courseName);
      if (spokenLanguageExam != null) {
        spokenLanguageCreditJSON.put("examId", spokenLanguageExam.getId());
        spokenLanguageCreditJSON.put("examGradeId", spokenLanguageExam.getGrade() != null ? spokenLanguageExam.getGrade().getId() : null);
        spokenLanguageCreditJSON.put("examSkillLevel", spokenLanguageExam.getSkillLevel() != null ? spokenLanguageExam.getSkillLevel().name() : null);
        spokenLanguageCreditJSON.put("examVerbalAssessment", spokenLanguageExam.getVerbalAssessment());
        spokenLanguageCreditJSON.put("examAssessorId", spokenLanguageExam.getAssessor() != null ? spokenLanguageExam.getAssessor().getId() : null);
        spokenLanguageCreditJSON.put("examAssessorName", spokenLanguageExam.getAssessor() != null ? spokenLanguageExam.getAssessor().getFullName() : null);
        
        Long examTimestamp = spokenLanguageExam.getTimestamp() != null ? DateUtils.toDate(spokenLanguageExam.getTimestamp().toLocalDate()).getTime() : null;
        spokenLanguageCreditJSON.put("examTimestamp", examTimestamp);
      }
      spokenLanguageCreditsJSONArray.add(spokenLanguageCreditJSON);
    }
    for (TransferCredit transferCredit : transferCredits) {
      // Validate here is for checking for the course codes that are applicable, skip all other courses
      if (!validate(student, transferCredit)) {
        continue;
      }
      
      SpokenLanguageExam spokenLanguageExam = spokenLanguageExamDAO.findBy(transferCredit);

      JSONObject spokenLanguageCreditJSON = new JSONObject();
      spokenLanguageCreditJSON.put("creditId", transferCredit.getId());
      spokenLanguageCreditJSON.put("courseName", transferCredit.getCourseName());
      if (spokenLanguageExam != null) {
        spokenLanguageCreditJSON.put("examId", spokenLanguageExam.getId());
        spokenLanguageCreditJSON.put("examGradeId", spokenLanguageExam.getGrade() != null ? spokenLanguageExam.getGrade().getId() : null);
        spokenLanguageCreditJSON.put("examSkillLevel", spokenLanguageExam.getSkillLevel() != null ? spokenLanguageExam.getSkillLevel().name() : null);
        spokenLanguageCreditJSON.put("examVerbalAssessment", spokenLanguageExam.getVerbalAssessment());
        spokenLanguageCreditJSON.put("examAssessorId", spokenLanguageExam.getAssessor() != null ? spokenLanguageExam.getAssessor().getId() : null);
        spokenLanguageCreditJSON.put("examAssessorName", spokenLanguageExam.getAssessor() != null ? spokenLanguageExam.getAssessor().getFullName() : null);
        
        Long examTimestamp = spokenLanguageExam.getTimestamp() != null ? DateUtils.toDate(spokenLanguageExam.getTimestamp().toLocalDate()).getTime() : null;
        spokenLanguageCreditJSON.put("examTimestamp", examTimestamp);
      }
      spokenLanguageCreditsJSONArray.add(spokenLanguageCreditJSON);
    }
    setJsDataVariable(requestContext, "credits", spokenLanguageCreditsJSONArray.toString());
    
    JSONArray skillLevelJSONArray = new JSONArray();
    for (SpokenLanguageExamSkillLevel skillLevel : SpokenLanguageExamSkillLevel.values()) {
      JSONObject skillLevelJSON = new JSONObject();
      skillLevelJSON.put("text", skillLevel.getValue());
      skillLevelJSON.put("value", skillLevel.name());
      skillLevelJSONArray.add(skillLevelJSON);
    }
    setJsDataVariable(requestContext, "skillLevels", skillLevelJSONArray.toString());
    
    requestContext.getRequest().setAttribute("student", student);
    requestContext.getRequest().setAttribute("transferCredits", transferCredits);
    requestContext.getRequest().setAttribute("gradingScales", gradingScales);
    
    requestContext.setIncludeJSP("/templates/grading/managespokenlanguageexams.jsp");
  }

  @Override
  public void processSend(PageRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    SpokenLanguageExamDAO spokenLanguageExamDAO = DAOFactory.getInstance().getSpokenLanguageExamDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CreditDAO creditDAO = DAOFactory.getInstance().getCreditDAO();

    boolean changed = false;
    Long studentId = requestContext.getLong("studentId");
    Student student = studentDAO.findById(studentId);

    if (student == null) {
      throw new SmvcRuntimeException(400, "Student not found.");
    }

    PyramusIxTableFacade creditTable = PyramusIxTableFacade.from(requestContext, "spokenLanguageExamsTable");
    
    for (PyramusIxTableRowFacade creditRow : creditTable.rows()) {
      if (Boolean.TRUE.equals(creditRow.getBoolean("edited"))) {
        Long creditId = creditRow.getLong("creditId");
        Long gradeId = creditRow.getLong("gradeId");
        SpokenLanguageExamSkillLevel skillLevel = creditRow.getEnum("skillLevel", SpokenLanguageExamSkillLevel.class);
        Long assessorId = creditRow.getLong("assessorId");
        LocalDateTime timestamp = creditRow.getLocalDateTime("timestamp");
        String verbalAssessment = creditRow.getString("verbalAssessment");
        
        Credit credit = creditDAO.findById(creditId);
        Grade grade = gradeDAO.findById(gradeId);
        StaffMember assessor = staffMemberDAO.findById(assessorId);
  
        SpokenLanguageExam spokenLanguageExam = spokenLanguageExamDAO.findBy(credit);
        
        if (credit == null || grade == null || skillLevel == null || timestamp == null || assessor == null) {
          throw new SmvcRuntimeException(400, "Student not found.");
        }
  
        if (!validate(student, credit)) {
          throw new SmvcRuntimeException(400, "Data validation failed.");
        }
        
        if (spokenLanguageExam != null) {
          changed = true;
          spokenLanguageExamDAO.update(spokenLanguageExam, grade, skillLevel, verbalAssessment, timestamp, assessor);
        } 
        else {
          changed = true;
          spokenLanguageExamDAO.create(credit, grade, skillLevel, verbalAssessment, timestamp, assessor);
        }
      }
    }
    
    if (changed) {
      KoskiController koskiController = CDI.current().select(KoskiController.class).get();
      koskiController.markForUpdate(student);
    }
    
    requestContext.setRedirectURL(String.format("%s/grading/managespokenexams.page?studentId=%d", requestContext.getRequest().getContextPath(), studentId));
  }

  private boolean validate(Student student, Credit credit) {
    String courseCode = null;
    if (credit instanceof TransferCredit) {
      TransferCredit transferCredit = (TransferCredit) credit;
      
      if (transferCredit.getSubject() != null && transferCredit.getCourseNumber() != null) {
        courseCode = transferCredit.getSubject().getCode() + transferCredit.getCourseNumber().toString();
      }
      
      if ((transferCredit.getStudent() == null) || !transferCredit.getStudent().getId().equals(student.getId())) {
        return false;
      }
    }
    else if (credit instanceof CourseAssessment) {
      CourseAssessment courseAssessment = (CourseAssessment) credit;
      
      if (courseAssessment.getSubject() != null && courseAssessment.getCourseNumber() != null) {
        courseCode = courseAssessment.getSubject().getCode() + courseAssessment.getCourseNumber().toString();
      }

      if ((courseAssessment.getStudent() == null) || !courseAssessment.getStudent().getId().equals(student.getId())) {
        return false;
      }
    }
    else {
      return false;
    }
    
    if (courseCode == null || !KoskiConsts.SPOKEN_LANGUAGE_EXAM_COURSECODES.contains(courseCode)) {
      return false;
    }
    
    return true;
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "grading.manageSpokenLanguageExams.pageTitle");
  }

}
