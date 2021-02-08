package fi.otavanopisto.pyramus.json.students;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactInfoDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class CopyStudentStudyProgrammeJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();

    Long studentId = requestContext.getLong("studentId");
    Student oldStudent = studentDAO.findById(studentId);
    
    StaffMember loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    
    if (!UserUtils.canAccessOrganization(loggedUser, oldStudent.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Cannot access specified student");
    }
    
    Long newStudyProgrammeId = requestContext.getLong("newStudyProgrammeId");
    StudyProgramme newStudyProgramme = studyProgrammeDAO.findById(newStudyProgrammeId);
    
    if (newStudyProgramme == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "New Study Programme not defined");
    }
    
    if (!UserUtils.canAccessOrganization(loggedUser, newStudyProgramme.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Cannot access specified study programme");
    }

    Boolean linkCredits = requestContext.getBoolean("linkCredits");
    Boolean setAsDefaultUser = requestContext.getBoolean("setAsDefaultUser");
    
    Person person = oldStudent.getPerson();
    String firstName = oldStudent.getFirstName();
    String lastName = oldStudent.getLastName();
    String nickname = oldStudent.getNickname();
    String additionalInfo = oldStudent.getAdditionalInfo();
    Double previousStudies = null; // student.getPreviousStudies();
    Date studyTimeEnd = null; // student.getStudyTimeEnd();
    Date studyStartTime = null; // student.getStudyStartDate();
    Date studyEndTime = null; // student.getStudyEndDate();
    String studyEndText = null; // student.getStudyEndText();
    Language language = oldStudent.getLanguage();
    Municipality municipality = oldStudent.getMunicipality();
    StudentActivityType activityType = oldStudent.getActivityType();
    StudentExaminationType examinationType = oldStudent.getExaminationType();
    StudentEducationalLevel educationalLevel = oldStudent.getEducationalLevel();
    String education = oldStudent.getEducation();
    Nationality nationality = oldStudent.getNationality();
    School school = oldStudent.getSchool();
    StudyProgramme studyProgramme = newStudyProgramme; // oldStudent.getStudyProgramme();
    StudentStudyEndReason studyEndReason = null; // student.getStudyEndReason();
    Curriculum curriculum = oldStudent.getCurriculum();

    Student newStudent = studentDAO.create(person, firstName, lastName, nickname, additionalInfo, studyTimeEnd,
        activityType, examinationType, educationalLevel, education, nationality, municipality, language, school, 
        studyProgramme, curriculum, previousStudies,studyStartTime, studyEndTime, studyEndReason, studyEndText, 
        false);
    
    // Variables are not copied, but the default values are applied
    
    userVariableDAO.createDefaultValueVariables(newStudent);
    
    // Contact info
    
    contactInfoDAO.update(newStudent.getContactInfo(), oldStudent.getContactInfo().getAdditionalInfo());
    
    // Default user
    
    if (person.getDefaultUser() == null || Boolean.TRUE.equals(setAsDefaultUser)) {
      personDAO.updateDefaultUser(person, newStudent);
    }
    
    // Addresses

    List<Address> addresses = oldStudent.getContactInfo().getAddresses();
    for (int i = 0; i < addresses.size(); i++) {
      Address add = addresses.get(i);
      addressDAO.create(newStudent.getContactInfo(), add.getContactType(), add.getName(), add.getStreetAddress(), add.getPostalCode(), add.getCity(),
          add.getCountry(), add.getDefaultAddress());
    }

    // Email addresses

    List<Email> emails = oldStudent.getContactInfo().getEmails();
    for (int i = 0; i < emails.size(); i++) {
      Email email = emails.get(i);
      emailDAO.create(newStudent.getContactInfo(), email.getContactType(), email.getDefaultAddress(), email.getAddress());
    }
    
    // Phone numbers

    List<PhoneNumber> phoneNumbers = oldStudent.getContactInfo().getPhoneNumbers();
    for (int i = 0; i < phoneNumbers.size(); i++) {
      PhoneNumber phoneNumber = phoneNumbers.get(i);
      phoneNumberDAO.create(newStudent.getContactInfo(), phoneNumber.getContactType(), phoneNumber.getDefaultNumber(), phoneNumber.getNumber());
    }

    if (linkCredits) {
      List<CourseAssessment> assessments = courseAssessmentDAO.listByStudent(oldStudent);
      for (CourseAssessment assessment : assessments) {
        creditLinkDAO.create(assessment, newStudent, loggedUser);
      }
      
      List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(oldStudent);
      for (TransferCredit transferCredit : transferCredits) {
        creditLinkDAO.create(transferCredit, newStudent, loggedUser);
      }
      
      List<CreditLink> creditLinks = creditLinkDAO.listByStudent(oldStudent);
      for (CreditLink creditLink : creditLinks) {
        creditLinkDAO.create(creditLink.getCredit(), newStudent, loggedUser);
      }
    }
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/students/editstudent.page?student=" + newStudent.getPerson().getId();
    String refererAnchor = requestContext.getRefererAnchor();

    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}