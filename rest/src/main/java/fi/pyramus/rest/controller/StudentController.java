package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.AddressDAO;
import fi.pyramus.dao.base.ContactInfoDAO;
import fi.pyramus.dao.base.ContactURLDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PhoneNumberDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.students.StudentVariableDAO;
import fi.pyramus.dao.students.StudentVariableKeyDAO;
import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.students.StudentVariable;
import fi.pyramus.domainmodel.students.StudentVariableKey;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentController {
  
  @Inject
  private StudentDAO studentDAO;

  @Inject
  private TagDAO tagDAO;

  @Inject
  private StudentVariableDAO studentVariableDAO;

  @Inject
  private StudentVariableKeyDAO studentVariableKeyDAO;

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

  public Student createStudent(AbstractStudent abstractStudent, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {

    Student student = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType,
        educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate, studyEndDate,
        studyEndReason, studyEndText, lodging);

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

  public List<Student> listUnarchivedStudents() {
    List<Student> students = studentDAO.listUnarchived();
    return students;
  }
  
  public List<Student> listStudentByAbstractStudent(AbstractStudent abstractStudent) {
    List<Student> students = studentDAO.listByAbstractStudent(abstractStudent);
    return students;
  }
  
  public Student updateStudent(Student student, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {
    
    studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType, educationalLevel,
        education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText,
        lodging);
    
    return student;
  }

  public Student updateStudentAbstractStudent(Student student, AbstractStudent abstractStudent) {
    if (!student.getAbstractStudent().getId().equals(abstractStudent.getId())) {
      return studentDAO.updateAbstractStudent(student, abstractStudent);
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
    studentDAO.delete(student);
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

  public synchronized Student updateStudentVariables(Student student, Map<String, String> variables) {
    Set<String> newKeys = new HashSet<>(variables.keySet());
    Set<String> oldKeys = new HashSet<>();
    Set<String> updateKeys = new HashSet<>();
    
    for (StudentVariable variable : student.getVariables()) {
      oldKeys.add(variable.getKey().getVariableKey());
    }

    for (String oldKey : oldKeys) {
      if (!newKeys.contains(oldKey)) {
        StudentVariableKey key = findStudentVariableKeyByVariableKey(oldKey);
        StudentVariable studentVariable = findStudentVariableByStudentAndKey(student, key);
        deleteStudentVariable(studentVariable);
      } else {
        updateKeys.add(oldKey);
      }
      
      newKeys.remove(oldKey);
    }
    
    for (String newKey : newKeys) {
      String value = variables.get(newKey);
      StudentVariableKey key = findStudentVariableKeyByVariableKey(newKey);
      createStudentVariable(student, key, value);
    }
    
    for (String updateKey : updateKeys) {
      String value = variables.get(updateKey);
      StudentVariableKey key = findStudentVariableKeyByVariableKey(updateKey);
      StudentVariable studentVariable = findStudentVariableByStudentAndKey(student, key);
      updateStudentVariable(studentVariable, value);
    }
    
    return student;
  }
  
  public StudentVariable createStudentVariable(Student student, StudentVariableKey key, String value) {
    return studentVariableDAO.create(student, key, value);
  }

  public StudentVariable findStudentVariableById(Long id) {
    StudentVariable studentVariable = studentVariableDAO.findById(id);
    return studentVariable;
  }

  public StudentVariable findStudentVariableByStudentAndKey(Student student, StudentVariableKey key) {
    return studentVariableDAO.findByStudentAndKey(student, key);
  }

  public void deleteStudentVariable(StudentVariable variable) {
    studentVariableDAO.delete(variable);
  }

  public StudentVariable updateStudentVariable(StudentVariable studentVariable, String value) {
    return studentVariableDAO.update(studentVariable, value);
  }
  
  public StudentVariableKey findStudentVariableKeyByVariableKey(String variableKey) {
    return studentVariableKeyDAO.findByKey(variableKey);
  }

  /* Email */

  public Email addStudentEmail(Student student, ContactType contactType, String address, Boolean defaultAddress) {
    return emailDAO.create(student.getContactInfo(), contactType, defaultAddress, address);
  }
  
  /* Address */

  public Address addStudentAddress(Student student, ContactType contactType, Boolean defaultAddress, String name, String streetAddress, String postalCode, String city, String country) {
    return addressDAO.create(student.getContactInfo(), contactType, name ,streetAddress, postalCode, city, country, defaultAddress);
  }

  /* PhoneNumber */

  public PhoneNumber addStudentPhoneNumber(Student student, ContactType contactType, String number, Boolean defaultNumber) {
    return phoneNumberDAO.create(student.getContactInfo(), contactType, defaultNumber, number);
  }
  
  /* ContactURL */

  public ContactURL addStudentContactURL(Student student, ContactURLType contactURLType, String url) {
    return contactURLDAO.create(student.getContactInfo(), contactURLType, url);
  }

}

