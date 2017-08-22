package fi.otavanopisto.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.SmvcRuntimeException;
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
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;

public class EditStudentJSONRequestController extends JSONRequestController {

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
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    
    Long personId = NumberUtils.createLong(requestContext.getRequest().getParameter("personId"));
    Person person = personDAO.findById(personId);

    Date birthday = requestContext.getDate("birthday");
    String ssecId = requestContext.getString("ssecId");
    Sex sex = "male".equals(requestContext.getRequest().getParameter("gender")) ? Sex.MALE : Sex.FEMALE;
    String basicInfo = requestContext.getString("basicInfo");
    Long version = requestContext.getLong("version"); 
    Boolean secureInfo = requestContext.getBoolean("secureInfo");
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");

    if (UserUtils.allowEditCredentials(loggedUser, person)) {
      if (!person.getVersion().equals(version))
        throw new StaleObjectStateException(Person.class.getName(), person.getId());
  
      boolean usernameBlank = StringUtils.isBlank(username);
      boolean passwordBlank = StringUtils.isBlank(password);
  
      if (!usernameBlank||!passwordBlank) {
        if (!passwordBlank) {
          if (!password.equals(password2))
            throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
        }
        
        // TODO: Support for multiple internal authentication providers
        List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
        if (internalAuthenticationProviders.size() == 1) {
          InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
          if (internalAuthenticationProvider != null) {
            UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
            
            if (internalAuthenticationProvider.canUpdateCredentials()) {
              if (userIdentification == null) {
                String externalId = internalAuthenticationProvider.createCredentials(username, password);
                userIdentificationDAO.create(person, internalAuthenticationProvider.getName(), externalId);
              } else {
                if ("-1".equals(userIdentification.getExternalId())) {
                  String externalId = internalAuthenticationProvider.createCredentials(username, password);
                  userIdentificationDAO.updateExternalId(userIdentification, externalId);
                } else {
                  if (!StringUtils.isBlank(username))
                    internalAuthenticationProvider.updateUsername(userIdentification.getExternalId(), username);
                
                  if (!StringUtils.isBlank(password))
                    internalAuthenticationProvider.updatePassword(userIdentification.getExternalId(), password);
                }
              }
            }
          }
        }
      }
    }
    
    // Abstract student
    personDAO.update(person, birthday, ssecId, sex, basicInfo, secureInfo);

    List<Student> students = studentDAO.listByPerson(person);

    for (Student student : students) {
      int rowCount = requestContext.getInteger("emailTable." + student.getId() + ".rowCount");
      for (int i = 0; i < rowCount; i++) {
        String colPrefix = "emailTable." + student.getId() + "." + i;
        String email = requestContext.getString(colPrefix + ".email");
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
        if (StringUtils.isNotBlank(email) && !UserUtils.isAllowedEmail(email, contactType, person.getId()))
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
      }
    }
    
    for (Student student : students) {
    	Long studentVersion = requestContext.getLong("studentVersion." + student.getId());
      if (!student.getVersion().equals(studentVersion))
        throw new StaleObjectStateException(Student.class.getName(), student.getId());

      String firstName = StringUtils.trim(requestContext.getString("firstName." + student.getId()));
	    String lastName = StringUtils.trim(requestContext.getString("lastName." + student.getId()));
	    String nickname = StringUtils.trim(requestContext.getString("nickname." + student.getId()));
	    String additionalInfo = requestContext.getString("additionalInfo." + student.getId());
	    String additionalContactInfo = requestContext.getString("otherContactInfo." + student.getId());
	    String education = requestContext.getString("education." + student.getId());
	    Double previousStudies = requestContext.getDouble("previousStudies." + student.getId());
	    Date studyTimeEnd = requestContext.getDate("studyTimeEnd." + student.getId());
	    Date studyStartDate = requestContext.getDate("studyStartDate." + student.getId());
	    Date studyEndDate = requestContext.getDate("studyEndDate." + student.getId());
	    String studyEndText = requestContext.getString("studyEndText." + student.getId());
	    Boolean lodging = "1".equals(requestContext.getString("lodging." + student.getId()));
	    String tagsText = requestContext.getString("tags." + student.getId());
	    
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
	    
	    Long entityId = requestContext.getLong("language." + student.getId());
	    Language language = entityId == null ? null : languageDAO.findById(entityId);
	
	    entityId = requestContext.getLong("activityType." + student.getId());
	    StudentActivityType activityType = entityId == null ? null : activityTypeDAO.findById(entityId);
	
	    entityId = requestContext.getLong("examinationType." + student.getId());
	    StudentExaminationType examinationType = entityId == null ? null : examinationTypeDAO.findById(entityId);
	
	    entityId = requestContext.getLong("educationalLevel." + student.getId());
	    StudentEducationalLevel educationalLevel = entityId == null ? null : educationalLevelDAO.findById(entityId);
	
	    entityId = requestContext.getLong("nationality." + student.getId());
	    Nationality nationality = entityId == null ? null : nationalityDAO.findById(entityId);
	
	    entityId = requestContext.getLong("municipality." + student.getId());
	    Municipality municipality = entityId == null ? null : municipalityDAO.findById(entityId);
	
	    entityId = requestContext.getLong("school." + student.getId());
	    School school = entityId != null && entityId > 0 ? schoolDAO.findById(entityId) : null;
	
	    entityId = requestContext.getLong("studyProgramme." + student.getId());
	    StudyProgramme studyProgramme = entityId != null && entityId > 0 ? studyProgrammeDAO.findById(entityId) : null;
	
	    entityId = requestContext.getLong("studyEndReason." + student.getId());
	    StudentStudyEndReason studyEndReason = entityId == null ? null : studyEndReasonDAO.findById(entityId);
	
      entityId = requestContext.getLong("curriculum." + student.getId());
      Curriculum curriculum = entityId == null ? null : curriculumDAO.findById(entityId);
	    
	    Integer variableCount = requestContext.getInteger("variablesTable." + student.getId() + ".rowCount");
	    if (variableCount != null) {
	      for (int i = 0; i < variableCount; i++) {
  	      String colPrefix = "variablesTable." + student.getId() + "." + i;
  	      String variableKey = requestContext.getString(colPrefix + ".key");
  	      String variableValue = requestContext.getString(colPrefix + ".value");
  	      userVariableDAO.setUserVariable(student, variableKey, variableValue);
  	    }
	    }
	    
	    // Student

	    studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd,
	        activityType, examinationType, educationalLevel, education, nationality, municipality, language, school, 
	        studyProgramme, curriculum, previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText, lodging);
	   
	    // Tags

	    studentDAO.setStudentTags(student, tagEntities);
	    
	    // Contact info
	    
	    contactInfoDAO.update(student.getContactInfo(), additionalContactInfo);
	    
	    // Student addresses
	    
	    Set<Long> existingAddresses = new HashSet<>();
	    int rowCount = requestContext.getInteger("addressTable." + student.getId() + ".rowCount");
	    for (int i = 0; i < rowCount; i++) {
	      String colPrefix = "addressTable." + student.getId() + "." + i;
	      Long addressId = requestContext.getLong(colPrefix + ".addressId");
	      Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
	      ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
	      String name = requestContext.getString(colPrefix + ".name");
	      String street = requestContext.getString(colPrefix + ".street");
	      String postal = requestContext.getString(colPrefix + ".postal");
	      String city = requestContext.getString(colPrefix + ".city");
	      String country = requestContext.getString(colPrefix + ".country");
	      boolean hasAddress = name != null || street != null || postal != null || city != null || country != null;
	      if (addressId == -1 && hasAddress) {
	        Address address = addressDAO.create(student.getContactInfo(), contactType, name, street, postal, city, country, defaultAddress);
	        existingAddresses.add(address.getId());
	      }
	      else if (addressId > 0) {
	        Address address = addressDAO.findById(addressId);
	        if (hasAddress) {
	          existingAddresses.add(addressId);
	          addressDAO.update(address, defaultAddress, contactType, name, street, postal, city, country);
	        }
	      }
	    }
	    List<Address> addresses = student.getContactInfo().getAddresses();
	    for (int i = addresses.size() - 1; i >= 0; i--) {
	      Address address = addresses.get(i);
	      if (!existingAddresses.contains(address.getId())) {
	        addressDAO.delete(address);
	      }
	    }

	    // Email addresses

	    Set<Long> existingEmails = new HashSet<>();
	    rowCount = requestContext.getInteger("emailTable." + student.getId() + ".rowCount");
	    for (int i = 0; i < rowCount; i++) {
	      String colPrefix = "emailTable." + student.getId() + "." + i;
        Boolean defaultAddress = requestContext.getBoolean(colPrefix + ".defaultAddress");
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
	      String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
	      
	      if (StringUtils.isNotBlank(email)) {
  	      Long emailId = requestContext.getLong(colPrefix + ".emailId");
  	      if (emailId == -1) {
  	        emailId = emailDAO.create(student.getContactInfo(), contactType, defaultAddress, email).getId(); 
  	      }
  	      else {
  	        emailDAO.update(emailDAO.findById(emailId), contactType, defaultAddress, email);
  	      }
  	      existingEmails.add(emailId);
	      }
	    }
	    List<Email> emails = student.getContactInfo().getEmails();
	    for (int i = emails.size() - 1; i >= 0; i--) {
	      Email email = emails.get(i);
	      if (!existingEmails.contains(email.getId())) {
	        emailDAO.delete(email);
	      }
	    }
	    
	    // Phone numbers
	    
      Set<Long> existingPhoneNumbers = new HashSet<>();
      rowCount = requestContext.getInteger("phoneTable." + student.getId() + ".rowCount");
      for (int i = 0; i < rowCount; i++) {
        String colPrefix = "phoneTable." + student.getId() + "." + i;
        Boolean defaultNumber = requestContext.getBoolean(colPrefix + ".defaultNumber");
        ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
        String number = requestContext.getString(colPrefix + ".phone");
        Long phoneId = requestContext.getLong(colPrefix + ".phoneId");
        if (phoneId == -1 && number != null) {
          phoneId = phoneNumberDAO.create(student.getContactInfo(), contactType, defaultNumber, number).getId();
          existingPhoneNumbers.add(phoneId);
        }
        else if (phoneId > 0 && number != null) {
          phoneNumberDAO.update(phoneNumberDAO.findById(phoneId), contactType, defaultNumber, number);
          existingPhoneNumbers.add(phoneId);
        }
      }
      List<PhoneNumber> phoneNumbers = student.getContactInfo().getPhoneNumbers();
      for (int i = phoneNumbers.size() - 1; i >= 0; i--) {
        PhoneNumber phoneNumber = phoneNumbers.get(i);
        if (!existingPhoneNumbers.contains(phoneNumber.getId())) {
          phoneNumberDAO.delete(phoneNumber);
        }
      }
    }

    // Contact information of a student won't be reflected to Person
    // used when searching students, so a manual re-index is needed

    person = personDAO.findById(person.getId());
    personDAO.forceReindex(person);
        
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}