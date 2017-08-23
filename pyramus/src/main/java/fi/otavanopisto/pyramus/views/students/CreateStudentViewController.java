package fi.otavanopisto.pyramus.views.students;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

public class CreateStudentViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    StudentActivityTypeDAO studentActivityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentEducationalLevelDAO studentEducationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentExaminationTypeDAO studentExaminationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    
    Long personId = pageRequestContext.getLong("personId");
    if (personId != null) {
      Person person = personDAO.findById(personId);
      StaffMember staffMember = staffMemberDAO.findByPerson(person);
      
      pageRequestContext.getRequest().setAttribute("person", person);
      pageRequestContext.getRequest().setAttribute("staffMember", staffMember);
      
      String emails = new JSONArrayExtractor("defaultAddress", "contactType", "address").extractString(staffMember.getContactInfo().getEmails());
      String addresses = new JSONArrayExtractor("defaultAddress", "name", "contactType", "streetAddress", "postalCode", "city", "country").extractString(staffMember.getContactInfo().getAddresses());
      String phones = new JSONArrayExtractor("defaultNumber", "contactType", "number").extractString(staffMember.getContactInfo().getPhoneNumbers());

      setJsDataVariable(pageRequestContext, "createstudent_emails", emails);
      setJsDataVariable(pageRequestContext, "createstudent_addresses", addresses);
      setJsDataVariable(pageRequestContext, "createstudent_phones", phones);
    }

    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));

    List<Nationality> nationalities = nationalityDAO.listUnarchived();
    Collections.sort(nationalities, new StringAttributeComparator("getName"));
    
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));
    
    List<Language> languages = languageDAO.listUnarchived();
    Collections.sort(languages, new StringAttributeComparator("getName"));

    List<School> schools = schoolDAO.listUnarchived();
    Collections.sort(schools, new StringAttributeComparator("getName"));

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));
    
    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    List<UserVariableKey> userVariableKeys = variableKeyDAO.listByUserEditable(Boolean.TRUE);
    Collections.sort(userVariableKeys, new StringAttributeComparator("getVariableName"));
    
    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));

    String jsonContactTypes = new JSONArrayExtractor("name", "id").extractString(contactTypes);
    String jsonVariableKeys = new JSONArrayExtractor("variableKey", "variableName", "variableType").extractString(userVariableKeys);
    
   	setJsDataVariable(pageRequestContext, "contactTypes", jsonContactTypes);
   	setJsDataVariable(pageRequestContext, "variableKeys", jsonVariableKeys);
    
    pageRequestContext.getRequest().setAttribute("schools", schools);
    pageRequestContext.getRequest().setAttribute("activityTypes", studentActivityTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("examinationTypes", studentExaminationTypeDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("educationalLevels", studentEducationalLevelDAO.listUnarchived());
    pageRequestContext.getRequest().setAttribute("nationalities", nationalities);
    pageRequestContext.getRequest().setAttribute("municipalities", municipalities);
    pageRequestContext.getRequest().setAttribute("languages", languages);
    pageRequestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    pageRequestContext.getRequest().setAttribute("curriculums", curriculums);
    pageRequestContext.getRequest().setAttribute("studyEndReasons", studyEndReasonDAO.listByParentReason(null));
    pageRequestContext.getRequest().setAttribute("variableKeys", userVariableKeys);
    
    pageRequestContext.setIncludeJSP("/templates/students/createstudent.jsp");
  }

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
    return Messages.getInstance().getText(locale, "students.createStudent.pageTitle");
  }

}
