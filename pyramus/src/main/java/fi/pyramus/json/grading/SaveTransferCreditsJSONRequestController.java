package fi.pyramus.json.grading;

import java.util.Date;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.StaffMember;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class SaveTransferCreditsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    GradeDAO gradeDAO = DAOFactory.getInstance().getGradeDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

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
      
      Grade grade = gradeDAO.findById(gradeId);
      Subject subject = subjectDAO.findById(subjectId);
      EducationalTimeUnit timeUnit = educationalTimeUnitDAO.findById(courseLengthUnitId);
      School school = schoolDAO.findById(schooId);
      StaffMember staffMember = staffMemberDAO.findById(userId);

      TransferCredit transferCredit;
      
      if (id != null && id >= 0) {
        transferCredit = transferCreditDAO.findById(id);
        transferCreditDAO.update(transferCredit, courseName, courseNumber, courseLength, timeUnit, school, subject, courseOptionality, student, staffMember, grade, date, transferCredit.getVerbalAssessment());
      } else {
        transferCredit = transferCreditDAO.create(courseName, courseNumber, courseLength, timeUnit, school, subject, courseOptionality, student, staffMember, grade, date, ""); 
      }
    }
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
