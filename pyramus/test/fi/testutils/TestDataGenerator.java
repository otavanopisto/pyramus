package fi.testutils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.pyramus.dao.BaseDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.OtherCost;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.pyramus.persistence.usertypes.Role;
import fi.pyramus.persistence.usertypes.Sex;

public class TestDataGenerator {

  public static List<User> createTestUsers(int count, int first, String firstNamePrefix, String lastnamePrefix, String emailName, String emailServer, String authProvider, Role role) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    
    List<User> result = new ArrayList<User>();
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(userDAO.createUser(firstNamePrefix + " #" + ind, lastnamePrefix + " #" + ind, String.valueOf(ind), authProvider, role));
    }
    
    return result;
  }
  
  public static List<Subject> createTestSubjects(int count, int first, String namePrefix) {
    BaseDAO baseDAO = new BaseDAO();
    
    List<Subject> result = new ArrayList<Subject>();
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(baseDAO.createSubject(ind + "", namePrefix + " #" + ind));
    }
    
    return result;
  }
  
  public static List<Module> createTestModules(int count, int first, String namePrefix, String descPrefix, User owner, Subject subject, EducationalTimeUnit timeUnit) {
    ModuleDAO moduleDAO = new ModuleDAO();
    
    List<Module> result = new ArrayList<Module>();
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(moduleDAO.createModule(namePrefix + " #" + ind, subject, 1, 12.0, timeUnit, descPrefix + " #" + ind, owner));
    }
    
    return result;
  }
  
  public static List<Course> createTestCourses(int count, int first, String namePrefix, String descPrefix, User owner, Subject subject, Module module, Date beginDate, Date endDate, Double courseLength, EducationalTimeUnit lengthTimeUnit, CourseState courseState) {
    CourseDAO courseDAO = new CourseDAO();
    
    List<Course> result = new ArrayList<Course>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(courseDAO.createCourse(module, namePrefix + " #" + ind, "", courseState, subject, 1, beginDate, endDate, courseLength, lengthTimeUnit, null, null, descPrefix + " #" + ind, owner));
    }
    
    return result;
  }
  
  public static List<CourseComponent> createTestCourseComponents(int count, int first, String namePrefix, String descPrefix, Course course, EducationalTimeUnit timeUnit) {
    CourseDAO courseDAO = new CourseDAO();
    
    List<CourseComponent> result = new ArrayList<CourseComponent>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(courseDAO.createCourseComponent(course, 12.0, timeUnit, namePrefix + " #" + ind, descPrefix + " #" + ind));
    }
    
    return result;
  }
  
  public static List<EducationType> createTestEducationTypes(int count, int first, String namePrefix) {
    BaseDAO baseDAO = new BaseDAO();
    
    List<EducationType> result = new ArrayList<EducationType>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(baseDAO.createEducationType(namePrefix + " #" + ind, "code" + ind));
    }
    
    return result;
  }
  
  public static List<EducationSubtype> createTestEducationSubtypes(int count, int first, EducationType educationType, String namePrefix) {
    BaseDAO baseDAO = new BaseDAO();
    
    List<EducationSubtype> result = new ArrayList<EducationSubtype>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(baseDAO.createEducationSubtype(educationType, namePrefix + " #" + ind, "code" + ind));
    }
    
    return result;
  }
  
  public static List<CourseEnrolmentType> createTestCourseEnrolmentTypes(int count, int first, String namePrefix) {
    CourseDAO courseDAO = new CourseDAO();
    
    List<CourseEnrolmentType> result = new ArrayList<CourseEnrolmentType>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(courseDAO.createCourseEnrolmentType(namePrefix + " #" + ind));
    }
    
    return result;
  }

  public static List<CourseParticipationType> createTestCourseParticipationTypes(int count, int first, String namePrefix) {
    CourseDAO courseDAO = new CourseDAO();
    
    List<CourseParticipationType> result = new ArrayList<CourseParticipationType>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(courseDAO.createCourseParticipationType(namePrefix + " #" + ind));
    }
    
    return result;
  }
  
  public static List<Student> createTestStudents(int count, int first, Date firstBirthDay, long firstDayIncMM, String firstNamePrefix, String lastnamePrefix, String emailPrefix) {
    StudentDAO studentDAO = new StudentDAO();
    
    List<Student> result = new ArrayList<Student>(); 
    
    long firstDayIncC = 0;
    Sex currentSex = Sex.FEMALE;
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      Date birthday = new Date(firstBirthDay.getTime() + firstDayIncC);
      String ssn = SSN_DATE_FORMAT.format(birthday) + '-' + SSN_END_FORMAT.format(ind);
      AbstractStudent abstractStudent = studentDAO.createAbstractStudent(birthday, ssn, currentSex);
      result.add(studentDAO.createStudent(abstractStudent, firstNamePrefix + " #" + ind, lastnamePrefix + " #" + ind, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
      firstDayIncC += firstDayIncMM;
      currentSex = currentSex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }
    
    return result;
  }
  
  public static List<OtherCost> createTestOtherCosts(int count, int first, Course course, String namePrefix, MonetaryAmount initialCost, Double costIncrement) {
    CourseDAO courseDAO = new CourseDAO();
    
    List<OtherCost> result = new ArrayList<OtherCost>(); 
    
    for (int i = 0; i < count; i++) {
      int ind = first + i;
      result.add(courseDAO.createOtherCost(course, namePrefix + " #" + ind, new MonetaryAmount(initialCost.getAmount() + (costIncrement * i))));
    }
    
    return result;
  }
  
  private final static DateFormat SSN_DATE_FORMAT = new SimpleDateFormat("ddMMyy");
  private final static NumberFormat SSN_END_FORMAT = new DecimalFormat("0000");

}
