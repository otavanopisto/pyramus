package fi.otavanopisto.pyramus.json.students;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
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
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentCardDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.InternalAuthDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
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
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Sex;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCard;
import fi.otavanopisto.pyramus.domainmodel.students.StudentCardType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentFunding;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.users.InternalAuth;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.framework.JSONRequestController2;
import fi.otavanopisto.pyramus.framework.PyramusRequestControllerAccess;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.security.impl.Permissions;
import fi.otavanopisto.pyramus.views.PyramusViewPermissions;

public class EditStudentJSONRequestController extends JSONRequestController2 {

  public EditStudentJSONRequestController() {
    super(
        PyramusRequestControllerAccess.REQUIRELOGIN // access
    );
  }

  @Override
  protected boolean checkAccess(RequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();

    Long loggedUserId = requestContext.getLoggedUserId();
    StaffMember staffMember = staffMemberDAO.findById(loggedUserId);

    if (!Permissions.instance().hasEnvironmentPermission(staffMember, PyramusViewPermissions.EDIT_STUDENT)) {
      return false;
    }
    else {
      Long personId = requestContext.getLong("personId");
      Person person = personDAO.findById(personId);

      // #1416: Staff members may only access students of their specified study programmes
      if (UserUtils.canAccessStudent(staffMember, person)) {
        if (UserUtils.canAccessAllOrganizations(staffMember)) {
          return true;
        }
        for (Student student : person.getStudents()) {
          if (UserUtils.isMemberOf(staffMember, student.getOrganization())) {
            // Having one common organization is enough - though the view may not allow editing all
            return true;
          }
        }
      }

      return false;
    }
  }
  
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
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();
    StudentLodgingPeriodDAO lodgingPeriodDAO = DAOFactory.getInstance().getStudentLodgingPeriodDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
    StudentStudyPeriodDAO studentStudyPeriodDAO = DAOFactory.getInstance().getStudentStudyPeriodDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentCardDAO studentCardDAO = DAOFactory.getInstance().getStudentCardDAO();

    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());
    
    Long personId = NumberUtils.createLong(requestContext.getRequest().getParameter("personId"));
    Person person = personDAO.findById(personId);
    
    Date birthday = requestContext.getDate("birthday");
    String ssecId = requestContext.getString("ssecId");
    Sex sex = (Sex) requestContext.getEnum("gender", Sex.class);
    String basicInfo = requestContext.getString("basicInfo");
    Long version = requestContext.getLong("version"); 
    Boolean secureInfo = requestContext.getBoolean("secureInfo");
    String username = requestContext.getString("username");
    String password = requestContext.getString("password1");
    String password2 = requestContext.getString("password2");

    if (UserUtils.allowEditCredentials(loggedUser, person)) {
      if (!person.getVersion().equals(version)) {
        throw new StaleObjectStateException(Person.class.getName(), person.getId());
      }
  
      boolean usernameBlank = StringUtils.isBlank(username);
      boolean passwordBlank = StringUtils.isBlank(password);

      UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson("internal", person);
      
      if (usernameBlank && passwordBlank) {
        // #1108: Existing credential deletion
        if (userIdentification != null && NumberUtils.isNumber(userIdentification.getExternalId())) {
          InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
          InternalAuth internalAuth = internalAuthDAO.findById(Long.valueOf(userIdentification.getExternalId()));
          if (internalAuth != null) {
            internalAuthDAO.delete(internalAuth);
          }
          userIdentificationDAO.delete(userIdentification);
        }
      }
      else if (!usernameBlank || !passwordBlank) {
        if (!passwordBlank && !password.equals(password2)) {
          throw new SmvcRuntimeException(PyramusStatusCode.PASSWORD_MISMATCH, "Passwords don't match");
        }

        // #921: Check username
        InternalAuthDAO internalAuthDAO = DAOFactory.getInstance().getInternalAuthDAO();
        InternalAuth internalAuth = internalAuthDAO.findByUsername(username);
        if (internalAuth != null) {
          userIdentification = userIdentificationDAO.findByAuthSourceAndExternalId("internal", internalAuth.getId().toString());
          if (userIdentification != null && !person.getId().equals(userIdentification.getPerson().getId())) {
            throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.usernameInUse"));
          }
        }
        else if (!usernameBlank && passwordBlank) {
          throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.nopassword"));
        }
        
        // TODO: Support for multiple internal authentication providers
        List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
        if (internalAuthenticationProviders.size() == 1) {
          InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
          if (internalAuthenticationProvider != null) {
            userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), person);
            
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

    // Person Variables
    
    Integer personVariableCount = requestContext.getInteger("personVariablesTable.rowCount");
    if (personVariableCount != null) {
      for (int i = 0; i < personVariableCount; i++) {
        String colPrefix = "personVariablesTable." + i;
        Long edited = requestContext.getLong(colPrefix + ".edited");
        if (Objects.equals(Long.valueOf(1), edited)) {
          String variableKey = requestContext.getString(colPrefix + ".key");
          String variableValue = requestContext.getString(colPrefix + ".value");
          personVariableDAO.setPersonVariable(person, variableKey, variableValue);
        }
      }
    }

    List<Student> students = UserUtils.canAccessAllOrganizations(loggedUser) ?
        studentDAO.listByPerson(person) : studentDAO.listByPersonAndOrganization(person, loggedUser.getOrganization());

    for (Student student : students) {
      int rowCount = requestContext.getInteger("emailTable." + student.getId() + ".rowCount");
      for (int i = 0; i < rowCount; i++) {
        String colPrefix = "emailTable." + student.getId() + "." + i;
        String email = StringUtils.trim(requestContext.getString(colPrefix + ".email"));
        if (StringUtils.isNotBlank(email)) {
          ContactType contactType = contactTypeDAO.findById(requestContext.getLong(colPrefix + ".contactTypeId"));
          
          if (!UserUtils.isAllowedEmail(email, contactType, person.getId()))
            throw new RuntimeException(Messages.getInstance().getText(requestContext.getRequest().getLocale(), "generic.errors.emailInUse"));
        }
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
      String parentBillingDetails = requestContext.getString("parentBillingDetails." + student.getId());
      String education = requestContext.getString("education." + student.getId());
      Double previousStudies = requestContext.getDouble("previousStudies." + student.getId());
      Date studyTimeEnd = requestContext.getDate("studyTimeEnd." + student.getId());
      Date studyStartDate = requestContext.getDate("studyStartDate." + student.getId());
      Date studyEndDate = requestContext.getDate("studyEndDate." + student.getId());
      String studyEndText = requestContext.getString("studyEndText." + student.getId());
      String tagsText = requestContext.getString("tags." + student.getId());
      StudentFunding funding = (StudentFunding) requestContext.getEnum("funding." + student.getId(), StudentFunding.class);
      
      Date studentCardExpires = requestContext.getDate("expiryDate." + student.getId());
      Boolean active = requestContext.getBoolean("active." + student.getId());
      StudentCardType studentCardType = (StudentCardType) requestContext.getEnum("studentCardType." + student.getId(), StudentCardType.class);
      
      
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

      entityId = requestContext.getLong("studyEndReason." + student.getId());
      StudentStudyEndReason studyEndReason = entityId == null ? null : studyEndReasonDAO.findById(entityId);
  
      entityId = requestContext.getLong("curriculum." + student.getId());
      Curriculum curriculum = entityId == null ? null : curriculumDAO.findById(entityId);

      entityId = requestContext.getLong("studyApprover." + student.getId());
      StaffMember approver = entityId == null ? null : staffMemberDAO.findById(entityId);

      Integer variableCount = requestContext.getInteger("variablesTable." + student.getId() + ".rowCount");
      if (variableCount != null) {
        for (int i = 0; i < variableCount; i++) {
          String colPrefix = "variablesTable." + student.getId() + "." + i;
          Long edited = requestContext.getLong(colPrefix + ".edited");
          if (Objects.equals(Long.valueOf(1), edited)) {
            String variableKey = requestContext.getString(colPrefix + ".key");
            String variableValue = requestContext.getString(colPrefix + ".value");
            userVariableDAO.setUserVariable(student, variableKey, variableValue);
          }
        }
      }

      Integer lodgingPeriodsCount = requestContext.getInteger("lodgingPeriodsTable." + student.getId() + ".rowCount");
      if (lodgingPeriodsCount != null) {
        Set<Long> remainingIds = new HashSet<>();
        
        for (int i = 0; i < lodgingPeriodsCount; i++) {
          String colPrefix = "lodgingPeriodsTable." + student.getId() + "." + i;
          
          Long id = requestContext.getLong(colPrefix + ".id");
          Date begin = requestContext.getDate(colPrefix + ".begin");
          Date end = requestContext.getDate(colPrefix + ".end");
          
          if (id == -1 && begin != null) {
            StudentLodgingPeriod lodgingPeriod = lodgingPeriodDAO.create(student, begin, end);
            remainingIds.add(lodgingPeriod.getId());
          } else if (id > 0) {
            StudentLodgingPeriod lodgingPeriod = lodgingPeriodDAO.findById(id);
            remainingIds.add(id);
            
            if (begin != null) {
              if (lodgingPeriod != null) {
                lodgingPeriodDAO.update(lodgingPeriod, begin, end);
              }
            }
          }
        }

        List<StudentLodgingPeriod> periods = lodgingPeriodDAO.listByStudent(student);
        periods.removeIf(period -> remainingIds.contains(period.getId()));
        periods.forEach(period -> lodgingPeriodDAO.delete(period));
      }
      
      Integer studyPeriodsCount = requestContext.getInteger("studentStudyPeriodsTable." + student.getId() + ".rowCount");
      if (studyPeriodsCount != null) {
        Set<Long> remainingIds = new HashSet<>();
        
        for (int i = 0; i < studyPeriodsCount; i++) {
          String colPrefix = "studentStudyPeriodsTable." + student.getId() + "." + i;
          
          Long id = requestContext.getLong(colPrefix + ".id");
          StudentStudyPeriodType periodType = (StudentStudyPeriodType) requestContext.getEnum(colPrefix + ".type", StudentStudyPeriodType.class);

          Date begin = requestContext.getDate(colPrefix + ".begin");
          // Null out the end date when period type allows only begin dates
          Date end = !StudentStudyPeriodType.BEGINDATE_ONLY.contains(periodType) ? requestContext.getDate(colPrefix + ".end") : null;
          
          if (id == -1 && begin != null) {
            StudentStudyPeriod studyPeriod = studentStudyPeriodDAO.create(student, begin, end, periodType);
            remainingIds.add(studyPeriod.getId());
          } else if (id > 0) {
            StudentStudyPeriod studyPeriod = studentStudyPeriodDAO.findById(id);
            remainingIds.add(id);
            
            if (begin != null) {
              if (studyPeriod != null) {
                studentStudyPeriodDAO.update(studyPeriod, begin, end, periodType);
              }
            }
          }
        }

        List<StudentStudyPeriod> periods = studentStudyPeriodDAO.listByStudent(student);
        periods.removeIf(period -> remainingIds.contains(period.getId()));
        periods.forEach(period -> studentStudyPeriodDAO.delete(period));
      }
      
      boolean studiesEnded = student.getStudyEndDate() == null && studyEndDate != null;
      
      // Student

      studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd,
          activityType, examinationType, educationalLevel, education, nationality, municipality, language, school, 
          curriculum, previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText);
      
      studentDAO.updateApprover(student, approver);
      studentDAO.updateFunding(student, funding);
      studentDAO.updateParentBillingDetails(student, parentBillingDetails);
      
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

      Long studyProgrammeId = student.getStudyProgramme() != null ? student.getStudyProgramme().getId() : null;
      // #4226: Remove applications of nettipk/nettilukio students when their studies end
      if (studiesEnded && studyProgrammeId != null && (studyProgrammeId == 6L || studyProgrammeId == 7L)) {
        ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
        Application application = applicationDAO.findByStudent(student);
        if (application != null) {
          ApplicationUtils.deleteApplication(application);
        }
      }
      
      // Student card
      
      StudentCard studentCard = studentCardDAO.findByStudent(student.getId());

      Date expiryDate = null;
      
      // Set expiry date automatically same as study end date or study time end
      if (student.getStudyEndDate() != null) {
        expiryDate = student.getStudyEndDate();
      }  else {
        expiryDate = student.getStudyTimeEnd();
      }
      
      if (studentCard != null && studentCardType != null) {
        // If user has set the expiry date manually we have to use it
        if (studentCardExpires != null && studentCard.getExpiryDate() != studentCardExpires) {
          expiryDate = studentCardExpires;
        }
        studentCardDAO.update(studentCard, active, expiryDate, studentCardType);
      } else {
        if (studentCardExpires != null) {
          expiryDate = studentCardExpires;
        }
        
        studentCardDAO.create(student, active, expiryDate, studentCardType);
        
      }
    }
    

    // Contact information of a student won't be reflected to Person
    // used when searching students, so a manual re-index is needed

    person = personDAO.findById(person.getId());
    personDAO.forceReindex(person);
        
    requestContext.setRedirectURL(requestContext.getReferer(true));
  }

}