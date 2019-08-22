package fi.otavanopisto.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactInfoDAO;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.LanguageDAO;
import fi.otavanopisto.pyramus.dao.base.MunicipalityDAO;
import fi.otavanopisto.pyramus.dao.base.NationalityDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class CreateStudentJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    StudentActivityTypeDAO activityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentExaminationTypeDAO examinationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentEducationalLevelDAO educationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    StudentLodgingPeriodDAO lodgingPeriodDAO = DAOFactory.getInstance().getStudentLodgingPeriodDAO();

    Long personId = requestContext.getLong("personId");
    
    int emailCount2 = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount2; i++) {
      String colPrefix = "emailTable." + i;
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
      if (StringUtils.isNotBlank(email)) {
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
        
        if (!UserUtils.isAllowedEmail(email, contactType, personId)) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
      }
    }
    
    Date birthday = requestContext.getDate("birthday");
    String ssecId = requestContext.getString("ssecId");
    Sex sex = (Sex) requestContext.getEnum("gender", Sex.class);
    String basicInfo = requestContext.getString("basicInfo");
    Boolean secureInfo = requestContext.getBoolean("secureInfo");
    String firstName = StringUtils.trim(requestContext.getString("firstName"));
    String lastName = StringUtils.trim(requestContext.getString("lastName"));
    String nickname = StringUtils.trim(requestContext.getString("nickname"));
    String additionalInfo = requestContext.getString("additionalInfo");
    String otherContactInfo = requestContext.getString("otherContactInfo");
    String education = requestContext.getString("education");
    Double previousStudies = requestContext.getDouble("previousStudies");
    Date studyTimeEnd = requestContext.getDate("studyTimeEnd");
    Date studyStartTime = requestContext.getDate("studyStartDate");
    Date studyEndTime = requestContext.getDate("studyEndDate");
    String studyEndText = requestContext.getString("studyEndText");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    Long entityId = requestContext.getLong("language");
    Language language = entityId == null ? null : languageDAO.findById(entityId);

    entityId = requestContext.getLong("municipality");
    Municipality municipality = entityId == null ? null : municipalityDAO.findById(entityId);

    entityId = requestContext.getLong("activityType");
    StudentActivityType activityType = entityId == null ? null : activityTypeDAO.findById(entityId);

    entityId = requestContext.getLong("examinationType");
    StudentExaminationType examinationType = entityId == null ? null : examinationTypeDAO.findById(entityId);

    entityId = requestContext.getLong("educationalLevel");
    StudentEducationalLevel educationalLevel = entityId == null ? null : educationalLevelDAO.findById(entityId);

    entityId = requestContext.getLong("nationality");
    Nationality nationality = entityId == null ? null : nationalityDAO.findById(entityId);

    entityId = requestContext.getLong("school");
    School school = entityId != null && entityId > 0 ? schoolDAO.findById(entityId) : null;

    entityId = requestContext.getLong("studyProgramme");
    StudyProgramme studyProgramme = entityId != null && entityId > 0 ? studyProgrammeDAO.findById(entityId) : null;

    entityId = requestContext.getLong("studyEndReason");
    StudentStudyEndReason studyEndReason = entityId == null ? null : studyEndReasonDAO.findById(entityId);

    entityId = requestContext.getLong("curriculum");
    Curriculum curriculum = entityId == null ? null : curriculumDAO.findById(entityId);
    
    Person person = personId != null ? personDAO.findById(personId) : null;
    Person personBySSN = personDAO.findBySSN(ssecId); 

    if (person == null) {
      if (personBySSN == null) {
        person = personDAO.create(birthday, ssecId, sex, basicInfo, secureInfo);
      }
      else {
        personDAO.update(personBySSN, birthday, ssecId, sex, basicInfo, secureInfo);
        person = personBySSN;
      }
    } else {
      personDAO.update(person, birthday, ssecId, sex, basicInfo, secureInfo);
    }
    
    Student student = studentDAO.create(person, firstName, lastName, nickname, additionalInfo, studyTimeEnd, 
        activityType, examinationType, educationalLevel, education, nationality, municipality, language, 
        school, studyProgramme, curriculum, previousStudies, studyStartTime, studyEndTime, studyEndReason, 
        studyEndText, false);

    // Lodging periods
    
    Integer lodgingPeriodsCount = requestContext.getInteger("lodgingPeriodsTable.rowCount");
    if (lodgingPeriodsCount != null) {
      for (int i = 0; i < lodgingPeriodsCount; i++) {
        String colPrefix = "lodgingPeriodsTable." + i;
        
        Date begin = requestContext.getDate(colPrefix + ".begin");
        Date end = requestContext.getDate(colPrefix + ".end");
        
        lodgingPeriodDAO.create(student, begin, end);
      }
    }
    
    // Tags

    studentDAO.setStudentTags(student, tagEntities);
    
    // Default user
    
    if(person.getDefaultUser() == null){
      personDAO.updateDefaultUser(person, student);
    }
    
    // Contact info
    
    contactInfoDAO.update(student.getContactInfo(), otherContactInfo);

    // Addresses
    
    int addressCount = requestContext.getInteger("addressTable.rowCount");
    for (int i = 0; i < addressCount; i++) {
      String colPrefix = "addressTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String name = requestContext.getString(colPrefix + ".name");
      String street = requestContext.getString(colPrefix + ".street");
      String postal = requestContext.getString(colPrefix + ".postal");
      String city = requestContext.getString(colPrefix + ".city");
      String country = requestContext.getString(colPrefix + ".country");
      boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;
      if (hasAddress) {
        addressDAO.create(student.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
      }
    }
    
    // Email addresses

    int emailCount = requestContext.getInteger("emailTable.rowCount");
    for (int i = 0; i < emailCount; i++) {
      String colPrefix = "emailTable." + i;
      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
      
      if (StringUtils.isNotBlank(email)) {
        emailDAO.create(student.getContactInfo(), contactType, defaultAddress, email);
      }
    }
    
    // Phone numbers

    int phoneCount = requestContext.getInteger("phoneTable.rowCount");
    for (int i = 0; i < phoneCount; i++) {
      String colPrefix = "phoneTable." + i;
      Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
      String number = requestContext.getString(colPrefix + ".phone");
      if (number != null) {
        phoneNumberDAO.create(student.getContactInfo(), contactType, defaultNumber, number);
      }
    }
    
    // Student variables

    Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
    if (variableCount != null) {
      for (int i = 0; i < variableCount; i++) {
        String colPrefix = "variablesTable." + i;
        String variableKey = requestContext.getRequest().getParameter(colPrefix + ".key");
        String variableValue = requestContext.getRequest().getParameter(colPrefix + ".value");
        userVariableDAO.setUserVariable(student, variableKey, variableValue);
      }
    }
    
    // Contact information of a student won't be reflected to Person
    // used when searching students, so a manual re-index is needed

    personDAO.forceReindex(student.getPerson());
    
    String redirectURL = requestContext.getRequest().getContextPath() + "/students/editstudent.page?person=" + student.getPerson().getId();
    String refererAnchor = requestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor)) {
      redirectURL += "#" + refererAnchor;
    }

    requestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}