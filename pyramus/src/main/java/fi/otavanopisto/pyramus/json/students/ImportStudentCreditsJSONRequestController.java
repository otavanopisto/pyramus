package fi.otavanopisto.pyramus.json.students;

import java.util.List;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ImportStudentCreditsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();

    Long studentId = requestContext.getLong("studentId");
    Student baseStudent = studentDAO.findById(studentId);
    
    List<Student> students = studentDAO.listByPerson(baseStudent.getPerson());
    students.remove(baseStudent);

    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());

    // LinkedCourseAssessments for baseStudent
    
    int rowCount = requestContext.getLong("linkedCourseAssessmentsTable." + baseStudent.getId() + ".rowCount").intValue();
    
    for (int i = 0; i < rowCount; i++) {
      String paramName = "linkedCourseAssessmentsTable." + baseStudent.getId() + "." + i + ".selected";
      boolean selected = new Long(1).equals(requestContext.getLong(paramName));
      
      if (selected) {
        Long creditLinkId = requestContext.getLong("linkedCourseAssessmentsTable." + baseStudent.getId() + "." + i + ".creditLinkId");
        
        CreditLink creditLink = creditLinkDAO.findById(creditLinkId);
        creditLinkDAO.archive(creditLink, loggedUser);
      }
    }

    // LinkedTransferCredits for baseStudent
    
    rowCount = requestContext.getLong("linkedTransferCreditsTable." + baseStudent.getId() + ".rowCount").intValue();
    
    for (int i = 0; i < rowCount; i++) {
      String paramName = "linkedTransferCreditsTable." + baseStudent.getId() + "." + i + ".selected";
      boolean selected = new Long(1).equals(requestContext.getLong(paramName));
      
      if (selected) {
        Long creditLinkId = requestContext.getLong("linkedTransferCreditsTable." + baseStudent.getId() + "." + i + ".creditLinkId");
        
        CreditLink creditLink = creditLinkDAO.findById(creditLinkId);
        creditLinkDAO.archive(creditLink, loggedUser);
      }
    }
    
    for (Student student : students) {
      // CourseAssessments
      
      rowCount = requestContext.getLong("courseAssessmentsTable." + student.getId() + ".rowCount").intValue();
      
      for (int i = 0; i < rowCount; i++) {
        String paramName = "courseAssessmentsTable." + student.getId() + "." + i + ".selected";
        boolean selected = new Long(1).equals(requestContext.getLong(paramName));
        
        if (selected) {
          Long courseAssessmentId = requestContext.getLong("courseAssessmentsTable." + student.getId() + "." + i + ".courseAssessmentId");
          
          CourseAssessment courseAssessment = courseAssessmentDAO.findById(courseAssessmentId);
          CreditLink creditLink = creditLinkDAO.findByStudentAndCredit(baseStudent, courseAssessment);
          
          if (creditLink == null)
            creditLinkDAO.create(courseAssessment, baseStudent, loggedUser);
        }
      }

      // TransferCredits
      
      rowCount = requestContext.getLong("transferCreditsTable." + student.getId() + ".rowCount").intValue();
      
      for (int i = 0; i < rowCount; i++) {
        String paramName = "transferCreditsTable." + student.getId() + "." + i + ".selected";
        boolean selected = new Long(1).equals(requestContext.getLong(paramName));
        
        if (selected) {
          Long transferCreditId = requestContext.getLong("transferCreditsTable." + student.getId() + "." + i + ".transferCreditId");
          
          TransferCredit transferCredit = transferCreditDAO.findById(transferCreditId);
          CreditLink creditLink = creditLinkDAO.findByStudentAndCredit(baseStudent, transferCredit);
          
          if (creditLink == null)
            creditLinkDAO.create(transferCredit, baseStudent, loggedUser);
        }
      }
      
      // LinkedCourseAssessments
      
      rowCount = requestContext.getLong("linkedCourseAssessmentsTable." + student.getId() + ".rowCount").intValue();
      
      for (int i = 0; i < rowCount; i++) {
        String paramName = "linkedCourseAssessmentsTable." + student.getId() + "." + i + ".selected";
        boolean selected = new Long(1).equals(requestContext.getLong(paramName));
        
        if (selected) {
          Long courseAssessmentId = requestContext.getLong("linkedCourseAssessmentsTable." + student.getId() + "." + i + ".courseAssessmentId");
          
          CourseAssessment courseAssessment = courseAssessmentDAO.findById(courseAssessmentId);
          CreditLink creditLink = creditLinkDAO.findByStudentAndCredit(baseStudent, courseAssessment);
          
          if (creditLink == null)
            creditLinkDAO.create(courseAssessment, baseStudent, loggedUser);
        }
      }

      // LinkedTransferCredits
      
      rowCount = requestContext.getLong("linkedTransferCreditsTable." + student.getId() + ".rowCount").intValue();
      
      for (int i = 0; i < rowCount; i++) {
        String paramName = "linkedTransferCreditsTable." + student.getId() + "." + i + ".selected";
        boolean selected = new Long(1).equals(requestContext.getLong(paramName));
        
        if (selected) {
          Long transferCreditId = requestContext.getLong("linkedTransferCreditsTable." + student.getId() + "." + i + ".transferCreditId");
          
          TransferCredit transferCredit = transferCreditDAO.findById(transferCreditId);
          CreditLink creditLink = creditLinkDAO.findByStudentAndCredit(baseStudent, transferCredit);
          
          if (creditLink == null)
            creditLinkDAO.create(transferCredit, baseStudent, loggedUser);
        }
      }
    }

    String redirectURL = requestContext.getRequest().getContextPath() + "/students/importstudentcredits.page?studentId=" + baseStudent.getId();
//    String refererAnchor = requestContext.getRefererAnchor();

//    if (!StringUtils.isBlank(refererAnchor))
//      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}