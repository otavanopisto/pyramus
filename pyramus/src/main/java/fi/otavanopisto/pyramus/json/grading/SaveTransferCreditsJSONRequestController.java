package fi.otavanopisto.pyramus.json.grading;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.grading.GradeDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveTransferCreditsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    Long studentId = jsonRequestContext.getLong("studentId");
    Student student = studentDAO.findById(studentId);

    int rowCount = jsonRequestContext.getInteger("transferCreditsTable.rowCount");
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "transferCreditsTable." + i;
      
      Long id = jsonRequestContext.getLong(colPrefix + ".creditId");
      String courseName = jsonRequestContext.getString(colPrefix + ".courseName");
      CourseOptionality courseOptionality = (CourseOptionality) jsonRequestContext.getEnum(colPrefix + ".courseOptionality", CourseOptionality.class);
      Integer courseNumber = jsonRequestContext.getInteger(colPrefix + ".courseNumber");
      Long gradeId = jsonRequestContext.getLong(colPrefix + ".grade");
      Long subjectId = jsonRequestContext.getLong(colPrefix + ".subject"); 
      Double courseLength = jsonRequestContext.getDouble(colPrefix + ".courseLength");
      Long courseLengthUnitId = jsonRequestContext.getLong(colPrefix + ".courseLengthUnit");
      Long schooId = jsonRequestContext.getLong(colPrefix + ".school");
      Date date = jsonRequestContext.getDate(colPrefix + ".date");
      Long userId = jsonRequestContext.getLong(colPrefix + ".user");
      Long curriculumId = jsonRequestContext.getLong(colPrefix + ".curriculum");
      boolean offCurriculum = new Long(1).equals(jsonRequestContext.getLong(colPrefix + ".offCurriculum"));
      
      Grade grade = gradeDAO.findById(gradeId);
      Subject subject = subjectDAO.findById(subjectId);
      EducationalTimeUnit timeUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);
      School school = schoolDAO.findById(schooId);
      StaffMember staffMember = staffMemberDAO.findById(userId);
      Curriculum curriculum = curriculumId != null ? curriculumDAO.findById(curriculumId) : null;

      TransferCredit transferCredit;
      
      if (id != null && id >= 0) {
        transferCredit = transferCreditDAO.findById(id);
        transferCreditDAO.update(transferCredit, courseName, courseNumber, courseLength, timeUnit, school, subject, 
            courseOptionality, student, staffMember, grade, date, transferCredit.getVerbalAssessment(), curriculum, offCurriculum);
      } else {
        transferCredit = transferCreditDAO.create(courseName, courseNumber, courseLength, timeUnit, school, subject, 
            courseOptionality, student, staffMember, grade, date, "", curriculum, offCurriculum);
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
