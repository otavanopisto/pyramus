package fi.otavanopisto.pyramus.rest.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.base.AddressDAO;
import fi.otavanopisto.pyramus.dao.base.ContactInfoDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.base.PhoneNumberDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.koski.KoskiClient;

@Dependent
@Stateless
public class StudentController {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentDAO studentDAO;

  @Inject
  private TagDAO tagDAO;

  @Inject
  private EmailDAO emailDAO;

  @Inject
  private PhoneNumberDAO phoneNumberDAO;
  
  @Inject
  private AddressDAO addressDAO;
  
  @Inject
  private ContactURLDAO contactURLDAO;

  @Inject
  private ContactInfoDAO contactInfoDAO;

  @Inject
  private PersonDAO personDAO;
  
  @Inject
  private TransferCreditDAO transferCreditDAO;

  @Inject
  private StudentLodgingPeriodDAO studentLodgingPeriodDAO;
  
  @Inject
  private CreditLinkDAO creditLinkDAO;

  @Inject
  private KoskiClient koskiClient;
  
  public Student createStudent(Person person, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Curriculum curriculum,
      Double previousStudies, Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText) {

    Student student = studentDAO.create(person, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType,
        educationalLevel, education, nationality, municipality, language, school, studyProgramme, curriculum, previousStudies, studyStartDate, 
        studyEndDate, studyEndReason, studyEndText, false);

    // Default user
    
    if (person.getDefaultUser() == null) {
      personDAO.updateDefaultUser(person, student);
    }
    
    return student;
  }

  public Student findStudentById(Long id) {
    Student student = studentDAO.findById(id);
    return student;
  }

  public List<Student> listStudents() {
    List<Student> students = studentDAO.listAll();
    return students;
  }

  public boolean hasCommonGroups(User user, Student student) {
    return studentDAO.hasCommonGroups(user, student);
  }
  
  public boolean isStudentGuider(StaffMember staffMember, Student student) {
    return studentDAO.isStudyGuider(staffMember, student);
  }
  
  public List<Student> listStudentByPerson(Person person) {
    List<Student> students = studentDAO.listByPerson(person);
    return students;
  }
  
  public Student updateStudent(Student student, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, Curriculum curriculum, 
      Double previousStudies, Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText) {
    
    studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType, educationalLevel,
        education, nationality, municipality, language, school, curriculum, previousStudies, studyStartDate, studyEndDate, 
        studyEndReason, studyEndText);
    
    return student;
  }

  public Student updateStudyProgramme(Student student, StudyProgramme studyProgramme) {
    try {
      koskiClient.invalidateAllStudentOIDs(student);
    } catch (Exception ex) {
      logger.log(Level.SEVERE, String.format("Invalidation of study permits for student %d failed", student.getId()), ex);
    }
    
    studentDAO.updateStudyProgramme(student, studyProgramme);
    return student;
  }

  public Student updateStudentPerson(Student student, Person person) {
    if (!student.getPerson().getId().equals(person.getId())) {
      return studentDAO.updatePerson(student, person);
    }
    
    return student;
  }
  
  public Student updateStudentAdditionalContactInfo(Student student, String additionalContactInfo) {
    contactInfoDAO.update(student.getContactInfo(), additionalContactInfo);
    return student;
  }

  public Student archiveStudent(Student student, User user) {
    studentDAO.archive(student, user);
    return student;
  }
  
  public Student unarchiveStudent(Student student, User user) {
    studentDAO.unarchive(student, user);
    return student;
  }

  public void deleteStudent(Student student) {
    Set<Tag> tags = new HashSet<>(student.getTags());
    for (Tag tag : tags) {
      studentDAO.removeTag(student, tag);
    }
    
    User defaultUser = student.getPerson().getDefaultUser();
    boolean newDefaultUser = false;
    Long personId = student.getPerson().getId();

    if (defaultUser != null) {
      if (defaultUser.getId().equals(student.getId())) {
        newDefaultUser = true;
        personDAO.updateDefaultUser(student.getPerson(), null);
      }
    }

    student = studentDAO.findById(student.getId());
    
    studentDAO.delete(student);

    // Do a best guess of the new default user
    if (newDefaultUser) {
      Person person = personDAO.findById(personId);
      
      Student latestStudent = person.getLatestStudent();
      if (latestStudent != null)
        personDAO.updateDefaultUser(person, latestStudent);
      else {
        if (!person.getStudents().isEmpty())
          personDAO.updateDefaultUser(person, person.getStudents().get(0));
        else if (!person.getStaffMembers().isEmpty())
          personDAO.updateDefaultUser(person, person.getStaffMembers().get(0));
      }
    }
  }
  
  /* Tags */
  
  public Tag createStudentTag(Student student, String text) {
    Tag tag = tagDAO.findByText(text);
    if (tag == null) {
      tag = tagDAO.create(text);
    }
    
    addStudentTag(student, tag);
    
    return tag;
  }
  
  public Student addStudentTag(Student student, Tag tag) {
    return studentDAO.addTag(student, tag);
  }

  public Student removeStudentTag(Student student, Tag tag) {
    return studentDAO.removeTag(student, tag);
  }

  public Student updateStudentTags(Student student, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> studentTags = new HashSet<>(student.getTags());
    
    for (Tag studentTag : studentTags) {
      if (!newTags.contains(studentTag.getText())) {
        removeStudentTag(student, studentTag);
      }
        
      newTags.remove(studentTag.getText());
    }
    
    for (String newTag : newTags) {
      createStudentTag(student, newTag);
    }
    
    return student;
  }

  /* Email */

  public List<Student> listStudents(Collection<Organization> organizations, String email, List<StudentGroup> groups, Boolean archived, Integer firstResult, Integer maxResults) {
    return studentDAO.listBy(organizations, email, groups, archived, firstResult, maxResults);
  }

  public Email addStudentEmail(Student student, ContactType contactType, String address, Boolean defaultAddress) throws UserEmailInUseException {
    // Trim the email address
    address = StringUtils.trim(address);

    if (StringUtils.isBlank(address)) {
      throw new IllegalArgumentException("Email cannot be blank.");
    }
    
    if (!UserUtils.isAllowedEmail(address, contactType, student.getPerson().getId()))
      throw new UserEmailInUseException();
    
    return emailDAO.create(student.getContactInfo(), contactType, defaultAddress, address);
  }
  
  public Email updateStudentEmail(Student student, Email email, ContactType contactType, String address, Boolean defaultAddress) throws UserEmailInUseException {
    // Trim the email address
    address = StringUtils.trim(address);

    if (StringUtils.isBlank(address)) {
      throw new IllegalArgumentException("Email cannot be blank.");
    }
    
    if (!UserUtils.isAllowedEmail(address, contactType, student.getPerson().getId())) {
      throw new UserEmailInUseException();
    }
    
    return emailDAO.update(email, contactType, defaultAddress, address);
  }
  
  /* Address */

  public Address addStudentAddress(Student student, ContactType contactType, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    return addressDAO.create(student.getContactInfo(), contactType, name ,streetAddress, postalCode, city, country, defaultAddress);
  }

  public Address updateStudentAddress(Address address, ContactType contactType, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    return addressDAO.update(address, defaultAddress, contactType, name, streetAddress, postalCode, city, country);
  }

  /* PhoneNumber */

  public PhoneNumber addStudentPhoneNumber(Student student, ContactType contactType, String number, Boolean defaultNumber) {
    return phoneNumberDAO.create(student.getContactInfo(), contactType, defaultNumber, number);
  }
  
  public PhoneNumber updateStudentPhoneNumber(PhoneNumber phoneNumber, ContactType contactType, String number, Boolean defaultNumber) {
    return phoneNumberDAO.update(phoneNumber, contactType, defaultNumber, number);
  }
  
  /* ContactURL */

  public ContactURL addStudentContactURL(Student student, ContactURLType contactURLType, String url) {
    return contactURLDAO.create(student.getContactInfo(), contactURLType, url);
  }
  
  /* TransferCredits */

  public List<TransferCredit> listStudentTransferCredits(Student student) {
    return transferCreditDAO.listByStudent(student);
  }
  
  public List<CreditLink> listStudentLinkedTransferCredits(Student student) {
    return creditLinkDAO.listByStudentAndType(student, CreditType.TransferCredit);
  }
  
  /* Lodging Period */

  public void addLodgingPeriod(Student student, Date begin, Date end) {
    studentLodgingPeriodDAO.create(student, begin, end);
  }
  
  public List<StudentLodgingPeriod> listLodgingPeriods(Student student) {
    return studentLodgingPeriodDAO.listByStudent(student);
  }
  
  public void deleteLodgingPeriod(StudentLodgingPeriod period) {
    studentLodgingPeriodDAO.delete(period);
  }

}

