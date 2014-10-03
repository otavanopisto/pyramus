package fi.pyramus.services;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EnumType;
import javax.xml.ws.BindingType;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactInfoDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentActivityTypeDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.dao.users.UserVariableDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.services.entities.EntityFactoryVault;
import fi.pyramus.services.entities.base.AddressEntity;
import fi.pyramus.services.entities.students.AbstractStudentEntity;
import fi.pyramus.services.entities.students.StudentEntity;

@Stateless
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@RolesAllowed("WebServices")
public class StudentsService extends PyramusService {

  public AbstractStudentEntity getAbstractStudentById(@WebParam (name="abstractStudentId") Long abstractStudentId) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    return EntityFactoryVault.buildFromDomainObject(abstractStudentDAO.findById(abstractStudentId));
  }

  public AbstractStudentEntity getAbstractStudentBySSN(@WebParam (name="ssn") String ssn) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    return EntityFactoryVault.buildFromDomainObject(abstractStudentDAO.findBySSN(ssn));
  }

  public StudentEntity addStudyProgramme(@WebParam (name="studentId") Long studentId, @WebParam (name="studyProgrammeId") Long studyProgrammeId) {
    // TODO Generalize to StudentDAO (also used in CopyStudentStudyProgrammeJSONRequestController)
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();

    Student oldStudent = studentDAO.findById(studentId);

    AbstractStudent abstractStudent = oldStudent.getAbstractStudent();
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
    StudyProgramme studyProgramme = studyProgrammeId == null ? null : studyProgrammeDAO.findById(studyProgrammeId);
    StudentStudyEndReason studyEndReason = null; // student.getStudyEndReason();
    Boolean lodging = false; // oldStudent.getLodging();

    Student newStudent = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType,
        educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartTime, studyEndTime,
        studyEndReason, studyEndText, lodging);

    // Contact info

    contactInfoDAO.update(newStudent.getContactInfo(), oldStudent.getContactInfo().getAdditionalInfo());

    // Addresses

    List<Address> addresses = oldStudent.getContactInfo().getAddresses();
    for (int i = 0; i < addresses.size(); i++) {
      Address add = addresses.get(i);
      addressDAO.create(newStudent.getContactInfo(), add.getContactType(), add.getName(), add.getStreetAddress(), add.getPostalCode(), add.getCity(),
          add.getCountry(), add.getDefaultAddress());
    }

    // E-mail addresses

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

    return EntityFactoryVault.buildFromDomainObject(newStudent);
  }

  public AbstractStudentEntity createAbstractStudent(@WebParam (name="birthday") Date birthday, @WebParam (name="socialSecurityNumber") String socialSecurityNumber, @WebParam (name="sex") String sex) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();

    // TODO: Parameterize?
    Boolean secureInfo = Boolean.FALSE;

    Sex studentSex = EnumType.valueOf(Sex.class, sex);
    AbstractStudent abstractStudent = abstractStudentDAO.create(birthday, socialSecurityNumber, studentSex, null, secureInfo);
    validateEntity(abstractStudent);
    return EntityFactoryVault.buildFromDomainObject(abstractStudent);
  }
  
  public void endStudentStudies(@WebParam (name="studentId") Long studentId, @WebParam (name = "endDate") Date endDate, @WebParam (name="endReasonId") Long endReasonId, @WebParam (name="endReasonText") String endReasonText) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentStudyEndReasonDAO endReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    
    Student student = studentDAO.findById(studentId);
    if (student != null) {
      StudentStudyEndReason endReason = endReasonId == null ? null : endReasonDAO.findById(endReasonId);
      studentDAO.endStudentStudies(student, endDate, endReason, endReasonText);
    }
  }

  public StudentEntity getStudentById(@WebParam (name="studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    return EntityFactoryVault.buildFromDomainObject(studentDAO.findById(studentId));
  }

  public StudentEntity[] listStudents() {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listUnarchived());
  }

  public AddressEntity[] listStudentsAddresses(@WebParam (name="studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    return (AddressEntity[]) EntityFactoryVault.buildFromDomainObjects(student.getContactInfo().getAddresses());
  }
  
  public StudentEntity[] listStudentsByStudyProgramme(@WebParam (name="studyProgrammeId") Long studyProgrammeId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
    
    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listByStudyProgramme(studyProgramme));
  }
  
  public StudentEntity[] listStudentsByAbstractStudent(@WebParam (name="abstractStudentId") Long abstractStudentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    
    AbstractStudent abstractStudent = abstractStudentDAO.findById(abstractStudentId);

    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listByAbstractStudent(abstractStudent));
  }
  
  public StudentEntity[] listActiveStudentsByAbstractStudent(@WebParam (name="abstractStudentId") Long abstractStudentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    
    AbstractStudent abstractStudent = abstractStudentDAO.findById(abstractStudentId);

    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listActiveStudentsByAbstractStudent(abstractStudent));
  }

  public StudentEntity[] listActiveStudents() {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listActiveStudents());
  }

  public StudentEntity[] listActiveStudentsByStudyProgramme(@WebParam (name="studyProgrammeId") Long studyProgrammeId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgramme studyProgramme = studyProgrammeDAO.findById(studyProgrammeId);
    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listActiveStudentsByStudyProgramme(studyProgramme));
  }
  
  public StudentEntity createStudent(
      @WebParam (name="abstractStudentId") Long abstractStudentId, 
      @WebParam (name="firstName") String firstName, 
      @WebParam (name="lastName") String lastName, 
      @WebParam (name="nickname") String nickname, 
      @WebParam (name="phone") String phone, 
      @WebParam (name="additionalInfo") String additionalInfo,
      @WebParam (name="parentalInfo") String parentalInfo, 
      @WebParam (name="studyTimeEnd") Date studyTimeEnd, 
      @WebParam (name="activityTypeId") Long activityTypeId, 
      @WebParam (name="examinationTypeId") Long examinationTypeId, 
      @WebParam (name="educationalLevelId") Long educationalLevelId, 
      @WebParam (name="education") String education, 
      @WebParam (name="nationalityId") Long nationalityId,
      @WebParam (name="municipalityId") Long municipalityId, 
      @WebParam (name="languageId") Long languageId, 
      @WebParam (name="schoolId") Long schoolId, 
      @WebParam (name="studyProgrammeId") Long studyProgrammeId, 
      @WebParam (name="previousStudies") Double previousStudies, 
      @WebParam (name="studyStartDate") Date studyStartDate, 
      @WebParam (name="studyEndDate") Date studyEndDate,
      @WebParam (name="studyEndReasonId") Long studyEndReasonId, 
      @WebParam (name="studyEndText") String studyEndText, 
      @WebParam (name="lodging") Boolean lodging) {

    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    StudentActivityTypeDAO activityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentExaminationTypeDAO examinationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentEducationalLevelDAO educationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    ContactInfoDAO contactInfoDAO = DAOFactory.getInstance().getContactInfoDAO();
    PhoneNumberDAO phoneNumberDAO = DAOFactory.getInstance().getPhoneNumberDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();

    AbstractStudent abstractStudent = abstractStudentDAO.findById(abstractStudentId);
    Nationality nationality = nationalityId == null ? null : nationalityDAO.findById(nationalityId);
    Municipality municipality = municipalityId == null ? null : municipalityDAO.findById(municipalityId);
    Language language = languageId == null ? null : languageDAO.findById(languageId);
    StudentActivityType activityType = activityTypeId == null ? null : activityTypeDAO.findById(activityTypeId);
    StudentExaminationType examinationType = examinationTypeId == null ? null : examinationTypeDAO.findById(examinationTypeId);
    StudentEducationalLevel educationalLevel = educationalLevelId == null ? null : educationalLevelDAO.findById(educationalLevelId);
    School school = schoolId == null ? null : schoolDAO.findById(schoolId);
    StudyProgramme studyProgramme = studyProgrammeId == null ? null : studyProgrammeDAO.findById(studyProgrammeId);
    StudentStudyEndReason studyEndReason = studyEndReasonId == null ? null : studyEndReasonDAO.findById(studyEndReasonId);

    Student student = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType,
        examinationType, educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate,
        studyEndDate, studyEndReason, studyEndText, lodging);
    
    // TODO Proper handling for phone and parental info
    
    if (phone != null) {
      ContactType contactType = contactTypeDAO.findById(new Long(1));
      phoneNumberDAO.create(student.getContactInfo(), contactType, Boolean.TRUE, phone);
    }
    
    contactInfoDAO.update(student.getContactInfo(), parentalInfo);

    validateEntity(student);
    return EntityFactoryVault.buildFromDomainObject(student);
  }

  public void updateStudent(@WebParam (name="studentId") Long studentId, @WebParam (name="firstName") String firstName, @WebParam (name="lastName") String lastName, @WebParam (name="nickname") String nickname, @WebParam (name="phone") String phone, @WebParam (name="additionalInfo") String additionalInfo, @WebParam (name="parentalInfo") String parentalInfo,
      @WebParam (name="studyTimeEnd") Date studyTimeEnd, @WebParam (name="activityTypeId") Long activityTypeId, @WebParam (name="examinationTypeId") Long examinationTypeId, @WebParam (name="educationalLevelId") Long educationalLevelId, @WebParam (name="education") String education, @WebParam (name="nationalityId") Long nationalityId, @WebParam (name="municipalityId") Long municipalityId,
      @WebParam (name="languageId") Long languageId, @WebParam (name="schoolId") Long schoolId, @WebParam (name="studyProgrammeId") Long studyProgrammeId, @WebParam (name="previousStudies") Double previousStudies, @WebParam (name="studyStartDate") Date studyStartDate, @WebParam (name="studyEndDate") Date studyEndDate, @WebParam (name="studyEndReasonId") Long studyEndReasonId,
      @WebParam (name="studyEndText") String studyEndText, @WebParam (name="lodging") Boolean lodging) {

    // TODO Get rid of phone number and parental info

    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentActivityTypeDAO activityTypeDAO = DAOFactory.getInstance().getStudentActivityTypeDAO();
    StudentExaminationTypeDAO examinationTypeDAO = DAOFactory.getInstance().getStudentExaminationTypeDAO();
    StudentEducationalLevelDAO educationalLevelDAO = DAOFactory.getInstance().getStudentEducationalLevelDAO();
    StudentStudyEndReasonDAO studyEndReasonDAO = DAOFactory.getInstance().getStudentStudyEndReasonDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    Student student = studentDAO.findById(studentId);
    Nationality nationality = nationalityId == null ? null : nationalityDAO.findById(nationalityId);
    Municipality municipality = municipalityId == null ? null : municipalityDAO.findById(municipalityId);
    Language language = languageId == null ? null : languageDAO.findById(languageId);
    StudentActivityType activityType = activityTypeId == null ? null : activityTypeDAO.findById(activityTypeId);
    StudentExaminationType examinationType = activityTypeId == null ? null : examinationTypeDAO.findById(examinationTypeId);
    StudentEducationalLevel educationalLevel = educationalLevelId == null ? null : educationalLevelDAO.findById(educationalLevelId);
    School school = schoolId == null ? null : schoolDAO.findById(schoolId);
    StudyProgramme studyProgramme = studyProgrammeId == null ? null : studyProgrammeDAO.findById(studyProgrammeId);
    StudentStudyEndReason studyEndReason = studyEndReasonId == null ? null : studyEndReasonDAO.findById(studyEndReasonId);

    studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType,
        educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate, studyEndDate,
        studyEndReason, studyEndText, lodging);

    validateEntity(student);
  }

  public void updateStudentMunicipality(@WebParam (name="studentId") Long studentId, @WebParam (name="municipalityId") Long municipalityId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();

    Student student = studentDAO.findById(studentId);
    Municipality municipality = municipalityId == null ? null : municipalityDAO.findById(municipalityId);

    studentDAO.updateStudentMunicipality(student, municipality);

    validateEntity(student);
  }
  
  public void addStudentAddress(@WebParam (name="studentId") Long studentId, @WebParam (name="addressType") String addressType, @WebParam (name="name") String name,@WebParam (name="streetAddress") String streetAddress, @WebParam (name="postalCode") String postalCode, @WebParam (name="city") String city, @WebParam (name="country") String country) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    AddressDAO addressDAO = DAOFactory.getInstance().getAddressDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    Student student = studentDAO.findById(studentId);
    // TODO contactType and default address
    ContactType contactType = contactTypeDAO.findById(new Long(1));
    Address address = addressDAO.create(student.getContactInfo(), contactType, name, streetAddress, postalCode, city, country, Boolean.TRUE);
    validateEntity(address);
  }

  public void addStudentEmail(@WebParam (name="studentId") Long studentId, @WebParam (name="defaultAddress") Boolean defaultAddress, @WebParam (name="address") String address) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    Student student = studentDAO.findById(studentId);
    // TODO contactType
    ContactType contactType = contactTypeDAO.findById(new Long(1));
    Email email = emailDAO.create(student.getContactInfo(), contactType, defaultAddress, address);
    validateEntity(email);
  }

  public void archiveStudent(@WebParam (name="studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    studentDAO.archive(student);
  }

  public void unarchiveStudent(@WebParam (name="studentId") Long studentId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    studentDAO.unarchive(student);
  }

  public void updateStudentEmail(@WebParam (name="studentId") Long studentId, @WebParam (name="fromAddress") String fromAddress, @WebParam (name="toAddress") String toAddress) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    Student student = studentDAO.findById(studentId);
    for (Email email : student.getContactInfo().getEmails()) {
      if (email.getAddress().equals(fromAddress)) {
        email = emailDAO.update(email, email.getContactType(), email.getDefaultAddress(), toAddress);
        validateEntity(email);
        break;
      }
    }
  }

  public void removeStudentEmail(@WebParam (name="studentId") Long studentId, @WebParam (name="address") String address) {
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    Student student = studentDAO.findById(studentId);
    for (Email email : student.getContactInfo().getEmails()) {
      if (email.getAddress().equals(address)) {
        emailDAO.delete(email);
        break;
      }
    }
  }

  public StudentEntity[] listStudentsByStudentVariable(@WebParam (name="key") String key, @WebParam (name="value") String value) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    return (StudentEntity[]) EntityFactoryVault.buildFromDomainObjects(studentDAO.listByUserVariable(key, value));
  }

  public String getStudentVariable(@WebParam (name="studentId") Long studentId, @WebParam (name="key") String key) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();

    Student student = studentDAO.findById(studentId);
    return userVariableDAO.findByUserAndKey(student, key);
  }

  public void setStudentSchool(@WebParam (name="studentId") Long studentId, @WebParam (name="schoolId") Long schoolId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();

    Student student = studentDAO.findById(studentId);
    School school = schoolId == null ? null : schoolDAO.findById(schoolId);
    studentDAO.updateSchool(student, school);
  }

  public void setStudentVariable(@WebParam (name="studentId") Long studentId, @WebParam (name="key") String key, @WebParam (name="value") String value) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    Student student = studentDAO.findById(studentId);
    userVariableDAO.setUserVariable(student, key, value);
  }

}
