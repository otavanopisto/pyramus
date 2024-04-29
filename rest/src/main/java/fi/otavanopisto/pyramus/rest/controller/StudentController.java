package fi.otavanopisto.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.students.StudentLodgingPeriodDAO;
import fi.otavanopisto.pyramus.dao.students.StudentStudyPeriodDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
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
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
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
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.users.ContactLogAccess;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserEmailInUseException;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.koski.KoskiClient;
import fi.otavanopisto.pyramus.rest.model.CourseActivity;
import fi.otavanopisto.pyramus.rest.model.CourseActivityAssessment;
import fi.otavanopisto.pyramus.rest.model.CourseActivityCurriculum;
import fi.otavanopisto.pyramus.rest.model.CourseActivityInfo;
import fi.otavanopisto.pyramus.rest.model.CourseActivityState;
import fi.otavanopisto.pyramus.rest.model.CourseActivitySubject;
import fi.otavanopisto.pyramus.security.impl.SessionController;

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
  private StudentStudyPeriodDAO studentStudyPeriodDAO;

  @Inject
  private CreditLinkDAO creditLinkDAO;

  @Inject
  private UserVariableDAO userVariableDAO;

  @Inject
  private CourseAssessmentDAO courseAssessmentDAO;
  
  @Inject
  private CourseAssessmentRequestDAO courseAssessmentRequestDAO;

  @Inject
  private KoskiClient koskiClient;
  
  @Inject
  private StudentGroupDAO studentGroupDAO;
  
  @Inject
  private SessionController sessionController;
  
  @Inject
  private UserController userController;
  
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
    
    userVariableDAO.createDefaultValueVariables(student);
    
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
    Long oldStudyProgrammeId = student.getStudyProgramme() != null ? student.getStudyProgramme().getId() : null;
    Long newStudyProgrammeId = studyProgramme != null ? studyProgramme.getId() : null;

    // Do nothing if studyprogramme has not changed
    if (!Objects.equals(oldStudyProgrammeId, newStudyProgrammeId)) {
      try {
        koskiClient.invalidateAllStudentOIDs(student);
      } catch (Exception ex) {
        logger.log(Level.SEVERE, String.format("Invalidation of study permits for student %d failed", student.getId()), ex);
      }
      
      studentDAO.updateStudyProgramme(student, studyProgramme);
    }
    
    return student;
  }
  
  public Student updateStudyTimeEnd(Student student, Date studyTimeEnd) {
    return studentDAO.updateStudyTimeEnd(student, studyTimeEnd);
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
  
  /* Course activity */
  
  public CourseActivityInfo listCourseActivities(Student student, List<CourseStudent> courseStudents, boolean includeTransferCredits) {
    List<CourseActivity> courseActivities = new ArrayList<>();
    List<CreditLink> linkedAssessments = creditLinkDAO.listByStudentAndType(student, CreditType.CourseAssessment); 
    
    // Go through the courses
    
    for (CourseStudent courseStudent : courseStudents) {
      Course course = courseStudent.getCourse();
      CourseAssessmentRequest request = courseAssessmentRequestDAO.findLatestByCourseStudent(courseStudent);
      
      // Course basic information
      
      CourseActivity courseActivity = new CourseActivity();
      courseActivity.setCourseId(course.getId());
      String courseName = course.getName();
      if (!StringUtils.isEmpty(course.getNameExtension())) {
        courseName = String.format("%s (%s)", courseName, course.getNameExtension());
      }
      courseActivity.setCourseName(courseName);
      
      // Curriculums
      
      List<CourseActivityCurriculum> curriculums = new ArrayList<>();
      for (Curriculum c : course.getCurriculums()) {
        CourseActivityCurriculum cac = new CourseActivityCurriculum();
        cac.setId(c.getId());
        cac.setName(c.getName());
        curriculums.add(cac);
      }
      courseActivity.setCurriculums(curriculums);

      // Subjects and their assessments, to be filled when going through course modules
      
      List<CourseActivitySubject> subjects = new ArrayList<>();
      List<CourseActivityAssessment> assessments = new ArrayList<>();
      courseActivity.setSubjects(subjects);
      courseActivity.setAssessments(assessments);
      
      for (CourseModule courseModule : course.getCourseModules()) {
        
        // Subject
        
        CourseActivitySubject subject = new CourseActivitySubject();
        subject.setCourseModuleId(courseModule.getId());
        if (courseModule.getCourseLength() != null) {
          subject.setCourseLength(courseModule.getCourseLength().getUnits());
          subject.setCourseLengthSymbol(courseModule.getCourseLength().getUnit().getSymbol());
        }
        subject.setCourseNumber(courseModule.getCourseNumber());
        if (courseModule.getSubject() != null) {
          subject.setSubjectCode(courseModule.getSubject().getCode());
          subject.setSubjectName(courseModule.getSubject().getName());
        }
        subjects.add(subject);
        
        // Assessment
        
        CourseActivityAssessment assessment = new CourseActivityAssessment();
        assessment.setDate(courseStudent.getEnrolmentTime());
        assessment.setState(CourseActivityState.ONGOING);
        assessment.setCourseModuleId(courseModule.getId());
        
        // Status override if course has been graded as linked credit
        // TODO: Should all linked credits be treated as transfer credits?
        
        CreditLink linkedAssessment = linkedAssessments.stream()
            .filter(creditLink -> course.getId().equals(((CourseAssessment) creditLink.getCredit()).getCourseStudent().getCourse().getId()))
            .findAny()
            .orElse(null);
        if (linkedAssessment != null) {
          assessment.setText(((CourseAssessment) linkedAssessment.getCredit()).getVerbalAssessment());
          assessment.setGrade(((CourseAssessment) linkedAssessment.getCredit()).getGrade().getName());
          assessment.setPassingGrade(((CourseAssessment) linkedAssessment.getCredit()).getGrade().getPassingGrade());
          assessment.setDate(linkedAssessment.getCreated());
          assessment.setGradeDate(linkedAssessment.getCredit().getDate());
          assessment.setState(CourseActivityState.GRADED_PASS);
        }

        // Status override if course has been graded
        
        CourseAssessment courseAssessment = courseAssessmentDAO.findLatestByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, Boolean.FALSE); 
        if (courseAssessment != null && courseAssessment.getGrade() != null) {
          assessment.setText(courseAssessment.getVerbalAssessment());
          assessment.setGrade(courseAssessment.getGrade().getName());
          assessment.setPassingGrade(courseAssessment.getGrade() == null || courseAssessment.getGrade().getPassingGrade());
          assessment.setDate(courseAssessment.getDate());
          assessment.setGradeDate(courseAssessment.getDate());
          assessment.setState(assessment.getPassingGrade() ? CourseActivityState.GRADED_PASS : CourseActivityState.GRADED_FAIL);
        }
        
        // Status override if student has requested the course to be assessed
        
        if (request != null && !request.getHandled() && request.getCreated().after(assessment.getDate())) {
          assessment.setText(request.getRequestText());
          assessment.setDate(request.getCreated());
          switch (assessment.getState()) {
          case GRADED_PASS:
            assessment.setState(CourseActivityState.ASSESSMENT_REQUESTED_PASSING_GRADE);
            break;
          case GRADED_FAIL:
            assessment.setState(CourseActivityState.ASSESSMENT_REQUESTED_FAILING_GRADE);
            break;
          default:
            assessment.setState(CourseActivityState.ASSESSMENT_REQUESTED_NO_GRADE);
            break;
          }
        }
        
        assessments.add(assessment);
        
      }
      courseActivities.add(courseActivity);
    }    

    // Optionally include transfer credits as well
    
    if (includeTransferCredits) {
      List<TransferCredit> transferCredits = listStudentTransferCredits(student);
      for (TransferCredit transferCredit : transferCredits) {
        
        // Basic info
        
        CourseActivity courseActivity = new CourseActivity();
        courseActivity.setCourseName(transferCredit.getCourseName());
        
        if (transferCredit.getOptionality() != null) {
          courseActivity.setOptionality(transferCredit.getOptionality().name());
        }
        // Curriculum

        if (transferCredit.getCurriculum() != null) {
          CourseActivityCurriculum cac = new CourseActivityCurriculum();
          cac.setId(transferCredit.getCurriculum().getId());
          cac.setName(transferCredit.getCurriculum().getName());
          courseActivity.setCurriculums(Arrays.asList(cac));
        }
        else {
          courseActivity.setCurriculums(Collections.emptyList());
        }
        
        // Subject
        
        CourseActivitySubject subject = new CourseActivitySubject();
        if (transferCredit.getCourseLength() != null) {
          subject.setCourseLength(transferCredit.getCourseLength().getUnits());
          subject.setCourseLengthSymbol(transferCredit.getCourseLength().getUnit().getSymbol());
        }
        subject.setCourseNumber(transferCredit.getCourseNumber());
        if (transferCredit.getSubject() != null) {
          subject.setSubjectCode(transferCredit.getSubject().getCode());
          subject.setSubjectName(transferCredit.getSubject().getName());
        }
        courseActivity.setSubjects(Arrays.asList(subject));
        
        // Assessment
        
        CourseActivityAssessment assessment = new CourseActivityAssessment();
        if (transferCredit.getGrade() != null) {
          assessment.setGrade(transferCredit.getGrade().getName());
          assessment.setPassingGrade(transferCredit.getGrade().getPassingGrade());
          assessment.setGradeDate(transferCredit.getDate());
        }
        assessment.setDate(transferCredit.getDate());
        assessment.setState(CourseActivityState.TRANSFERRED);
        courseActivity.setAssessments(Arrays.asList(assessment));
        
        courseActivities.add(courseActivity);
      }
      
      // Linked transfer credits

      List<CreditLink> linkedTransferCredits = includeTransferCredits
          ? listStudentLinkedTransferCredits(student)
          : Collections.emptyList();
      for (CreditLink creditLink : linkedTransferCredits) {
        TransferCredit transferCredit = (TransferCredit) creditLink.getCredit();
        
        // Basic info
        
        CourseActivity courseActivity = new CourseActivity();
        courseActivity.setCourseName(transferCredit.getCourseName());
        
        // Optionality
        if (transferCredit.getOptionality() != null) {
          courseActivity.setOptionality(transferCredit.getOptionality().name());
        }
        
        // Curriculum

        if (transferCredit.getCurriculum() != null) {
          CourseActivityCurriculum cac = new CourseActivityCurriculum();
          cac.setId(transferCredit.getCurriculum().getId());
          cac.setName(transferCredit.getCurriculum().getName());
          courseActivity.setCurriculums(Arrays.asList(cac));
        }
        else {
          courseActivity.setCurriculums(Collections.emptyList());
        }
        
        // Subject
        
        CourseActivitySubject subject = new CourseActivitySubject();
        if (transferCredit.getCourseLength() != null) {
          subject.setCourseLength(transferCredit.getCourseLength().getUnits());
          subject.setCourseLengthSymbol(transferCredit.getCourseLength().getUnit().getSymbol());
        }
        subject.setCourseNumber(transferCredit.getCourseNumber());
        if (transferCredit.getSubject() != null) {
          subject.setSubjectCode(transferCredit.getSubject().getCode());
          subject.setSubjectName(transferCredit.getSubject().getName());
        }
        courseActivity.setSubjects(Arrays.asList(subject));
        
        // Assessment

        CourseActivityAssessment assessment = new CourseActivityAssessment();
        if (transferCredit.getGrade() != null) {
          assessment.setGrade(transferCredit.getGrade().getName());
          assessment.setPassingGrade(transferCredit.getGrade().getPassingGrade());
          assessment.setGradeDate(transferCredit.getDate());
        }
        assessment.setDate(transferCredit.getDate());
        assessment.setState(CourseActivityState.TRANSFERRED);
        courseActivity.setAssessments(Arrays.asList(assessment));

        courseActivities.add(courseActivity);
      }
    }
    
    // Basic info about the student complemented with the activity list above
    
    CourseActivityInfo courseActivityInfo = new CourseActivityInfo();
    courseActivityInfo.setLineName(student.getStudyProgramme().getName());
    courseActivityInfo.setLineCategory(student.getStudyProgramme().getCategory().getName());
    courseActivityInfo.setDefaultLine(student.getPerson().getDefaultUser().getId().equals(student.getId()));
    courseActivityInfo.setActivities(courseActivities);
    
    return courseActivityInfo;
  }
  
  /* Study Period */
  
  public EnumSet<StudentStudyPeriodType> getActiveStudyPeriods(Student student, Date date) {
    List<StudentStudyPeriod> studyPeriods = studentStudyPeriodDAO.listByStudent(student);
    
    List<StudentStudyPeriod> filteredPeriods = studyPeriods.stream()
        .filter(studyPeriod -> studyPeriod != null && studyPeriod.getBegin() != null)
        .sorted(Comparator.comparing(StudentStudyPeriod::getBegin))
        .collect(Collectors.toList());

    EnumSet<StudentStudyPeriodType> result = EnumSet.noneOf(StudentStudyPeriodType.class);
    
    for (StudentStudyPeriod studyPeriod : filteredPeriods) {
      // Potentially (may be cancelled by another period) active period?
      boolean isPotentiallyActivePeriod = 
          (date.equals(studyPeriod.getBegin()) || date.after(studyPeriod.getBegin())) && 
          (studyPeriod.getEnd() == null || date.equals(studyPeriod.getEnd()) || date.before(studyPeriod.getEnd()));
      
      if (isPotentiallyActivePeriod) {
        switch (studyPeriod.getPeriodType()) {
          case COMPULSORY_EDUCATION:
            // Compulsory and non-compulsory are mutually exclusive - the latter period cancels the previous 
            result.add(StudentStudyPeriodType.COMPULSORY_EDUCATION);
            result.remove(StudentStudyPeriodType.NON_COMPULSORY_EDUCATION);
          break;
          
          case NON_COMPULSORY_EDUCATION:
            // Compulsory and non-compulsory are mutually exclusive - the latter period cancels the previous 
            result.add(StudentStudyPeriodType.NON_COMPULSORY_EDUCATION);
            result.remove(StudentStudyPeriodType.COMPULSORY_EDUCATION);
          break;
            
          case PROLONGED_STUDYENDDATE:
          case TEMPORARILY_SUSPENDED:
          case EXTENDED_COMPULSORY_EDUCATION:
            result.add(studyPeriod.getPeriodType());
          break;
        }
      }
    }

    return result;
  }
  
  /* Checks if logged user is guidance counselor of specific student */
  public boolean amIGuidanceCounselor(Long studentId, StaffMember staffMember) {
    
    if (staffMember == null) {
      return false;
    }
    
    Student student = findStudentById(studentId);
    // List of student's user groups
    List<StudentGroup> studentGroups = studentGroupDAO.listByStudentAndUserAndGuidanceGroupAndArchived(student, staffMember, true, false);
    
    return !studentGroups.isEmpty();
  }

  public ContactLogAccess resolveContactLogAccess(Student student) {
    User loggedUser = sessionController.getUser();
    
    if (loggedUser.hasRole(Role.ADMINISTRATOR)) {
      return ContactLogAccess.ALL;
    } 
    StaffMember staffMember = userController.findStaffMemberById(loggedUser.getId());
    
    if (staffMember == null) {
      return ContactLogAccess.NONE;
    } else {
      if (!staffMember.isAccountEnabled()) {
        return ContactLogAccess.NONE;
      }
    }
    
    Boolean amIGuidanceCounselor = amIGuidanceCounselor(student.getId(), staffMember);
    
    if (amIGuidanceCounselor) {
      return ContactLogAccess.ALL;
    } else {
      return ContactLogAccess.OWN;
    }
  }
}

