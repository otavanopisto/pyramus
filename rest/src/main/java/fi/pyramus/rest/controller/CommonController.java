package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.BillingDetailsDAO;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.ContactURLDAO;
import fi.pyramus.dao.base.ContactURLTypeDAO;
import fi.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.pyramus.dao.base.EducationSubtypeDAO;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.grading.GradeDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.UserUtils;
import fi.pyramus.persistence.search.SearchResult;

@Dependent
@Stateless
public class CommonController {
  
  @Inject
  private EducationTypeDAO educationTypeDAO;
  
  @Inject
  private EducationSubtypeDAO educationSubtypeDAO;
  
  @Inject
  private SubjectDAO subjectDAO;

  @Inject
  private GradingScaleDAO gradingScaleDAO;
  
  @Inject
  private GradeDAO gradeDAO;

  @Inject
  private EducationalTimeUnitDAO educationalTimeUnitDAO;

  @Inject
  private CourseBaseVariableKeyDAO courseBaseVariableKeyDAO;

  @Inject
  private EmailDAO emailDAO;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private PhoneNumberDAO phoneNumberDAO;
  
  @Inject
  private ContactURLTypeDAO contactURLTypeDAO;
  
  @Inject
  private ContactURLDAO contactURLDAO;

  @Inject
  private ContactTypeDAO contactTypeDAO;

  @Inject
  private BillingDetailsDAO billingDetailsDAO;
  
  /* EducationType */
  
  public EducationType createEducationType(String name, String code) {
    EducationType educationType = educationTypeDAO.create(name, code);
    return educationType;
  }
  
  public List<EducationType> listEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listAll();
    return educationTypes;
  }
  
  public List<EducationType> listUnarchivedEducationTypes() {
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    return educationTypes;
  }
  
  public EducationType findEducationTypeById(Long id) {
    EducationType educationType = educationTypeDAO.findById(id);
    return educationType;
  }
  
  public EducationType updateEducationType(EducationType educationType, String name, String code) {
    educationTypeDAO.update(educationType, name, code);
    return educationType;
  }
  
  public EducationType archiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.archive(educationType, user);
    return educationType;
  }
  
  public EducationType unarchiveEducationType(EducationType educationType, User user) {
    educationTypeDAO.unarchive(educationType, user);
    return educationType;
  }

  public void deleteEducationType(EducationType educationType) {
    educationTypeDAO.delete(educationType);
  }
  
  /* EducationSubtype */

  public EducationSubtype createEducationSubtype(EducationType educationType, String name, String code) {
    return educationSubtypeDAO.create(educationType, name, code);
  }
  
  public EducationSubtype findEducationSubtypeById(Long id) {
    return educationSubtypeDAO.findById(id);
  }
  
  public List<EducationSubtype> listEducationSubtypesByEducationType(EducationType educationType) {
    return educationSubtypeDAO.listByEducationType(educationType);
  }
  
  public EducationSubtype updateEducationSubtype(EducationSubtype educationSubtype, EducationType educationType, String name, String code) {
    educationSubtype = educationSubtypeDAO.update(educationSubtype, name, code);

    if (!educationType.getId().equals(educationSubtype.getEducationType().getId())) {
      educationSubtype = educationSubtypeDAO.updateEducationType(educationSubtype, educationType);
    }

    return educationSubtype;
  }
  
  public EducationSubtype archiveEducationSubtype(EducationSubtype educationSubtype, User user) {
    educationSubtypeDAO.archive(educationSubtype, user);
    return educationSubtype;
  }
  
  public void deleteEducationSubtype(EducationSubtype educationSubtype) {
    educationSubtypeDAO.delete(educationSubtype);
  }
  
  /* GradingScale */
  
  public GradingScale createGradingScale(String name, String description) {
    GradingScale gradingScale = gradingScaleDAO.create(name, description);
    return gradingScale;
  }
  
  public List<GradingScale> listGradingScales() {
    List<GradingScale> gradingScales = gradingScaleDAO.listAll();
    return gradingScales;
  }
  
  public List<GradingScale> listUnarchivedGradingScales() {
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();
    return gradingScales;
  }
  
  public GradingScale findGradingScaleById(Long id) {
    GradingScale gradingScale = gradingScaleDAO.findById(id);
    return gradingScale;
  }
  
  public GradingScale archiveGradingScale(GradingScale gradingScale, User user) {
    gradingScaleDAO.archive(gradingScale, user);
    return gradingScale;
  }
  
  public GradingScale unarchiveGradingScale(GradingScale gradingScale, User user) {
    gradingScaleDAO.unarchive(gradingScale, user);
    return gradingScale;
  }

  public void deleteGradingScale(GradingScale gradingScale) {
    gradingScaleDAO.delete(gradingScale);
  }
  
  /* Grade */
  
  public Grade createGrade(GradingScale gradingScale, String name, String description, Boolean passingGrade, Double gpa, String qualification) {
    Grade grade = gradeDAO.create(gradingScale, name, description, passingGrade, gpa, qualification);
    return grade;
  }
  
  public Grade findGradeByIdId(Long id) {
    return gradeDAO.findById(id);
  }
  
  public Grade updateGrade(Grade grade, String name, String description, Boolean passingGrade, Double gpa, String qualification) {
    return gradeDAO.update(grade, name, description, passingGrade, gpa, qualification);
  }
  
  public Grade archiveGrade(Grade grade, User user) {
    gradeDAO.archive(grade, user);
    return grade;
  }
  
  public Grade unarchiveGrade(Grade grade, User user) {
    gradeDAO.unarchive(grade, user);
    return grade;
  }

  public void deleteGrade(Grade grade) {
    gradeDAO.delete(grade);
  }
  
  /* EducationalTimeUnit */
  
  public EducationalTimeUnit createEducationalTimeUnit(Double baseUnits, String name) {
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.create(baseUnits, name);
    return educationalTimeUnit;
  }  
  
  public EducationalTimeUnit findEducationalTimeUnitById(Long id) {
    EducationalTimeUnit educationalTimeUnit = educationalTimeUnitDAO.findById(id);
    return educationalTimeUnit;
  }

  public List<EducationalTimeUnit> listEducationalTimeUnits() {
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listAll();
    return educationalTimeUnits;
  }
  
  public List<EducationalTimeUnit> listUnarchivedEducationalTimeUnits() {
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    return educationalTimeUnits;
  }
  
  public EducationalTimeUnit updateEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, Double baseUnits, String name) {
    educationalTimeUnitDAO.update(educationalTimeUnit, baseUnits, name);
    return educationalTimeUnit;
  }
  
  public EducationalTimeUnit archiveEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, User user) {
    educationalTimeUnitDAO.archive(educationalTimeUnit, user);
    return educationalTimeUnit;
  }
  
  public EducationalTimeUnit unarchiveEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit, User user) {
    educationalTimeUnitDAO.unarchive(educationalTimeUnit, user);
    return educationalTimeUnit;
  }
  
  public void deleteEducationalTimeUnit(EducationalTimeUnit educationalTimeUnit) {
    educationalTimeUnitDAO.delete(educationalTimeUnit);
  }
  
  public Subject createSubject(String code, String name, EducationType educationType) {
    Subject subject = subjectDAO.create(code, name, educationType);
    return subject;
  }
  
  public List<Subject> listSubjects() {
    List<Subject> subjects = subjectDAO.listAll();
    return subjects;
  }
  
  public List<Subject> listUnarchivedSubjects() {
    List<Subject> subjects = subjectDAO.listUnarchived();
    return subjects;
  }
  
  public List<Subject> listSubjectsByEducationType(EducationType educationType) {
    List<Subject> subjects = subjectDAO.listByEducationType(educationType);
    return subjects;
  }
  
  public Subject findSubjectById(Long id) {
    Subject subject = subjectDAO.findById(id);
    return subject;
  }
  
  public SearchResult<Subject> searchSubjects(int resultsPerPage, int page, String text) {
    SearchResult<Subject> subjects = subjectDAO.searchSubjectsBasic(resultsPerPage, page, text);
    return subjects;
  }
  
  public Subject updateSubject(Subject subject, String code, String name, EducationType educationType) {
    subjectDAO.update(subject, code, name, educationType);
    return subject;
  }
  
  public GradingScale updateGradingScale(GradingScale gradingScale, String name, String description) {
    gradingScaleDAO.update(gradingScale, name, description);
    return gradingScale;
  }
  
  public Subject archiveSubject(Subject subject, User user) {
    subjectDAO.archive(subject, user);
    return subject;
  }
  
  public Subject unarchiveSubject(Subject subject, User user) {
    subjectDAO.unarchive(subject, user);
    return subject;
  }

  public void deleteSubject(Subject subject) {
    subjectDAO.delete(subject);
  }
  
  /* CourseBaseVariableKey */

  public CourseBaseVariableKey createCourseBaseVariableKey(String key, String name, VariableType variableType, Boolean userEditable) {
    return courseBaseVariableKeyDAO.create(key, name, variableType, userEditable);
  }
  
  public CourseBaseVariableKey findCourseBaseVariableKeyByVariableKey(String variableKey) {
    return courseBaseVariableKeyDAO.findByVariableKey(variableKey);
  }
  
  public List<CourseBaseVariableKey> listCourseBaseVariableKeys() {
    return courseBaseVariableKeyDAO.listAll();
  }

  public CourseBaseVariableKey updateCourseBaseVariableKey(CourseBaseVariableKey courseBaseVariableKey, String variableName, VariableType variableType, Boolean userEditable) {
    return 
        courseBaseVariableKeyDAO.updateUserEditable(
            courseBaseVariableKeyDAO.updateVariableName(
                courseBaseVariableKeyDAO.updateVariableType(courseBaseVariableKey, variableType), variableName), userEditable);
  }

  public void deleteCourseBaseVariableKey(CourseBaseVariableKey courseBaseVariableKey) {
    courseBaseVariableKeyDAO.delete(courseBaseVariableKey);
  }
  
  /* Email */

  // TODO: allowed email check is better handled when email is created through staffmember or student, maybe it should be always context-aware creation
  public Email createEmail(ContactInfo contactInfo, ContactType contactType, Boolean defaultAddress, String address) {
    if (!UserUtils.isAllowedEmail(address, contactType))
      throw new RuntimeException("Email address is in use.");

    return emailDAO.create(contactInfo, contactType, defaultAddress, address);
  }
  
  public Email findEmailById(Long id) {
    return emailDAO.findById(id);
  }
  
  public void deleteEmail(Email email) {
    emailDAO.delete(email); 
  }
  
  /* ContactType */
  
  public ContactType createContactType(String name, Boolean nonUnique) {
    return contactTypeDAO.create(name, nonUnique);
  }

  public ContactType findContactTypeById(Long contactTypeId) {
    return contactTypeDAO.findById(contactTypeId);
  }

  public ContactType updateContactType(ContactType contactType, String name, Boolean nonUnique) {
    return contactTypeDAO.update(contactType, name, nonUnique);
  }

  public void archiveContactType(ContactType contactType, User user) {
    contactTypeDAO.archive(contactType, user);
  }

  public List<ContactType> listUnarchivedContactTypes() {
    return contactTypeDAO.listUnarchived();
  }

  public List<ContactType> listContactTypes() {
    return contactTypeDAO.listAll();
  }

  public void deleteContactType(ContactType contactType) {
    contactTypeDAO.delete(contactType);
  }
  
  /* Address */

  public Address createAddress(ContactInfo contactInfo, ContactType contactType, String name, String streetAddress, String postalCode, String city, String country, Boolean defaultAddress) {
    return addressDAO.create(contactInfo, contactType, name, streetAddress, postalCode, city, country, defaultAddress);
  }
  
  public Address findAddressById(Long id) {
    return addressDAO.findById(id);
  }
  
  public void deleteAddress(Address address) {
    addressDAO.delete(address);
  }
  
  /* PhoneNumber */
  
  public PhoneNumber createPhoneNumber(ContactInfo contactInfo, ContactType contactType, Boolean defaultNumber, String number) {
    return phoneNumberDAO.create(contactInfo, contactType, defaultNumber, number);
  }
  
  public PhoneNumber findPhoneNumberById(Long id) {
    return phoneNumberDAO.findById(id);
  }
  
  public void deletePhoneNumber(PhoneNumber phoneNumber) {
    phoneNumberDAO.delete(phoneNumber); 
  }
  
  /* ContactURLType */

  public ContactURLType createContactURLType(String name) {
    return contactURLTypeDAO.create(name);
  }

  public ContactURLType findContactURLTypeById(Long contactURLTypeId) {
    return contactURLTypeDAO.findById(contactURLTypeId);
  }

  public ContactURLType updateContactURLType(ContactURLType contactURLType, String name) {
    return contactURLTypeDAO.update(contactURLType, name);
  }

  public void archiveContactURLType(ContactURLType contactURLType, User user) {
    contactURLTypeDAO.archive(contactURLType, user);
  }

  public List<ContactURLType> listUnarchivedContactURLTypes() {
    return contactURLTypeDAO.listUnarchived();
  }

  public List<ContactURLType> listContactURLTypes() {
    return contactURLTypeDAO.listAll();
  }

  public void deleteContactURLType(ContactURLType contactURLType) {
    contactURLTypeDAO.delete(contactURLType);
  }
  
  /* ContactURL */

  public ContactURL findContactURLById(Long id) {
    return contactURLDAO.findById(id);
  }

  public void deleteContactURL(ContactURL contactURL) {
    contactURLDAO.delete(contactURL);
  }
  
  /* BillingDetails */

  public BillingDetails findBillingDetailsById(Long billingDetailsId) {
    return billingDetailsDAO.findById(billingDetailsId);
  }
}
