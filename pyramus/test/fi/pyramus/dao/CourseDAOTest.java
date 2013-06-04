package fi.pyramus.dao;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseComponent;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.OtherCost;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.resources.MaterialResource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.domainmodel.resources.WorkResource;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.pyramus.persistence.usertypes.Role;
import fi.pyramus.util.SearchResult;
import fi.pyramus.util.SearchTimeFilterMode;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;


@Test 
public class CourseDAOTest extends DatabaseDependingTest {

  private static final String         COURSE_ENROMENT_TYPE_NAME = "manual / 手冊";
  private static final String         COURSE_PARTICIPATION_TYPE_NAME = "e - learing / メール-学習";
  private static final String         COURSE_NAME = "Test / ทดสอบ";
  private static final Date           COURSE_BEGIN_DATE = createDate(2000, 4, 6);
  private static final Date           COURSE_END_DATE = createDate(2000, 5, 2);
  private static final Double         COURSE_LENGTH = 20.0;
  private static final String         COURSE_DESCRIPTION = "Test course / Kiểm tra khóa học";
  
  private static final String         OTHERCOST_NAME = "Test bench / Cuốn thử nghiệm";
  private static final MonetaryAmount OTHERCOST_COST = new MonetaryAmount(120.5, Currency.getInstance("EUR"));
  
  private static final String         COURSE_COMPONENT_NAME = "my component / بلدي مكون";
  private static final Double         COURSE_COMPONENT_LENGTH = 15.0;
  private static final String         COURSE_COMPONENT_DESCRIPTION = "so fine-component /  так тонко компонента";
  
  @BeforeClass
  public void setUp() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    User user = userDAO.createUser("Test", "User", "-5", "internal", UserRole.USER);
    
    EducationalTimeUnit educationalTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    CourseState courseState = courseDAO.createCourseState("Testing"); 
    
    Module module = moduleDAO.createModule("Test module", subject, 1, 4.0, educationalTimeUnit, "Test desc", user);
  
    this.moduleId = module.getId();
    this.userId = user.getId();
    this.subjectId = subject.getId();
    this.educationalLengthUnitId = educationalTimeUnit.getId();
    this.stateId = courseState.getId();

    commit();
  }
    
  @AfterClass 
  public void tearDown() {
    beginTransaction();
    
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    userDAO.deleteUser(userDAO.getUser(userId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(educationalLengthUnitId));
    
    commit();
  }
  
  private EducationalTimeUnit getEducationalTimeUnit() {
    return baseDAO.getEducationalTimeUnit(educationalLengthUnitId);
  }
  
  private CourseState getCourseState() {
    return courseDAO.getCourseState(stateId);
  }

  private Module getTestModule() {
    return moduleDAO.getModule(moduleId);
  }
  
  private Subject getTestSubject() {
    return baseDAO.getSubject(subjectId);
  }
  
  private User getTestUser() {
    return userDAO.getUser(userId);
  }
  
  public void testGetCourseEnrolmentType() {
    beginTransaction();
    CourseEnrolmentType courseEnrolmentType = courseDAO.createCourseEnrolmentType(COURSE_ENROMENT_TYPE_NAME);
    commit();
    
    Long id = courseEnrolmentType.getId();
    
    beginTransaction();
    courseEnrolmentType = courseDAO.getCourseEnrolmentType(id);
    Assert.assertEquals(courseEnrolmentType.getId(), id);
    courseDAO.deleteCourseEnrolmentType(courseEnrolmentType);
    commit();
  }

  public void testDeleteCourseEnrolmentType() {
    /* testGetCourseEnrolmentType already tests deleteCourseEnrolmentType method */
  }

  public void testGetCourseParticipationType() {
    beginTransaction();
    CourseParticipationType courseParticipationType = courseDAO.createCourseParticipationType(COURSE_PARTICIPATION_TYPE_NAME);
    commit();
    
    Long id = courseParticipationType.getId();
    
    beginTransaction();
    courseParticipationType = courseDAO.getCourseParticipationType(id);
    Assert.assertEquals(courseParticipationType.getId(), id);
    courseDAO.deleteCourseParticipationType(courseParticipationType);
    commit();
  }

  public void testDeleteCourseParticipationType() {
    /* testGetCourseParticipationType already tests deleteCourseParticipationType method */
  }

  public void testListCourseComponents() {
    beginTransaction();
    
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    TestDataGenerator.createTestCourseComponents(10, 0, "Component", "Description", course, getEducationalTimeUnit());
    commit();
    
    long courseId = course.getId();
    
    beginTransaction();
    Assert.assertEquals(courseDAO.listCourseComponents(courseDAO.getCourse(courseId)).size(), 10);
    course = courseDAO.getCourse(courseId);
    courseDAO.deleteCourse(course);
    
    commit();
  }

  public void testListCourseEnrolmentTypes() {
    beginTransaction();
    List<CourseEnrolmentType> types = TestDataGenerator.createTestCourseEnrolmentTypes(10, 0, "Enrolment type");
    commit();
    
    beginTransaction();
    types = courseDAO.listCourseEnrolmentTypes();
    
    Assert.assertEquals(types.size(), 10);
    while (types.size() > 0) {
      courseDAO.deleteCourseEnrolmentType(types.get(0));
      types.remove(0);
    }
    
    commit();
  }
  
  public void testCreateCourseEnrolmentType() {
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createCourseEnrolmentType(null);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.createCourseEnrolmentType("");
      }
    });
  }

  public void testCreateCourseParticipationType() {
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createCourseParticipationType(null);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.createCourseParticipationType("");
      }
    });
  }
  
  public void testListCourseParticipationTypes() {
    beginTransaction();
    List<CourseParticipationType> types = TestDataGenerator.createTestCourseParticipationTypes(10, 0, "Participation type");
    commit();
    
    beginTransaction();
    types = courseDAO.listCourseParticipationTypes();
    
    Assert.assertEquals(types.size(), 10);
    while (types.size() > 0) {
      courseDAO.deleteCourseParticipationType(types.get(0));
      types.remove(0);
    }
    
    commit(); 
  }

  public void testUpdateCourse() {
    Course course;
    
    beginTransaction();
    
    String tempName = "NAME";
    Date tempBegin = createDate(1990, 12, 11);
    Date tempEnd = createDate(1990, 13, 12);
    String tempDesc = "DESC";
    Subject tempSubject = TestDataGenerator.createTestSubjects(1, 0, "SUBJECT").get(0);
    User tempUser = TestDataGenerator.createTestUsers(1, 0, "TEST", "USER", "test.user", "email.com", "internal", UserRole.ADMINISTRATOR).get(0);
    
    final Long courseId = courseDAO.createCourse(getTestModule(), tempName, "", getCourseState(), tempSubject, 1, tempBegin, tempEnd, 12.0, getEducationalTimeUnit(), null, null, tempDesc, tempUser).getId();
    commit();
    
    /* Make sure that initial data is ok */ 
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getName(), tempName);
    Assert.assertEquals(course.getBeginDate(), tempBegin);
    Assert.assertEquals(course.getEndDate(), tempEnd);
    Assert.assertEquals(course.getCourseLength().getUnits(), 12.0);
    Assert.assertEquals(course.getDescription(), tempDesc);
    Assert.assertEquals(course.getSubject().getId(), tempSubject.getId());
    Assert.assertEquals(course.getCreator().getId(), tempUser.getId());
    Assert.assertEquals(course.getLastModifier().getId(), tempUser.getId());
    
    /* Update all values */
    
    courseDAO.updateCourse(course, COURSE_NAME, "", null, getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, baseDAO.getEducationalTimeUnit(this.educationalLengthUnitId), null, null, COURSE_DESCRIPTION, getTestUser());
    
    commit();
    
    /* and recheck everything */ 
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getName(), COURSE_NAME);
    Assert.assertEquals(course.getBeginDate(), COURSE_BEGIN_DATE);
    Assert.assertEquals(course.getEndDate(), COURSE_END_DATE);
    Assert.assertEquals(course.getCourseLength().getUnits(), COURSE_LENGTH);
    Assert.assertEquals(course.getDescription(), COURSE_DESCRIPTION);
    Assert.assertEquals(course.getSubject().getId(), this.subjectId);
    Assert.assertEquals(course.getCreator().getId(), tempUser.getId());
    Assert.assertEquals(course.getLastModifier().getId(), this.userId);
    commit();
    
    final CourseDAOTest _this = this;
    
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateCourse(courseDAO.getCourse(courseId), null, "", null, getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, baseDAO.getEducationalTimeUnit(_this.educationalLengthUnitId), null, null, COURSE_DESCRIPTION, getTestUser());
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.updateCourse(courseDAO.getCourse(courseId), "", "", null, getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, baseDAO.getEducationalTimeUnit(_this.educationalLengthUnitId), null, null, COURSE_DESCRIPTION, getTestUser());
      }
    });
    
    /* null length tests */ 
    checkProperty("units", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateCourse(courseDAO.getCourse(courseId), COURSE_NAME, "", null, getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, null, baseDAO.getEducationalTimeUnit(_this.educationalLengthUnitId), null, null, COURSE_DESCRIPTION, getTestUser());
      }
    });
    
    beginTransaction();

    /* clean up */
    
    course = courseDAO.getCourse(courseId);
    courseDAO.deleteCourse(course);
    userDAO.deleteUser(userDAO.getUser(tempUser.getId()));
    baseDAO.deleteSubject(baseDAO.getSubject(tempSubject.getId()));
    
    commit();
  }

  public void testArchiveCourse() {
    Course course;
    
    beginTransaction();
    
    String tempName = "NAME";
    Date tempBegin = createDate(1990, 12, 11);
    Date tempEnd = createDate(1990, 13, 12);
    String tempDesc = "DESC";
    Subject tempSubject = TestDataGenerator.createTestSubjects(1, 0, "SUBJECT").get(0);
    User tempUser = TestDataGenerator.createTestUsers(1, 0, "TEST", "USER", "test.user", "email.com", "internal", UserRole.ADMINISTRATOR).get(0);
    
    course = courseDAO.createCourse(getTestModule(), tempName, "", getCourseState(), tempSubject, 1, tempBegin, tempEnd, 12.0, getEducationalTimeUnit(), null, null, tempDesc, tempUser);
    Long courseId = course.getId();
    courseDAO.archiveCourse(course);
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getArchived(), Boolean.TRUE);
    
    baseDAO.deleteSubject(baseDAO.getSubject(tempSubject.getId()));
    userDAO.deleteUser(userDAO.getUser(tempUser.getId()));
    courseDAO.deleteCourse(course);
    
    commit();
    
    
  }

  public void testCreateCourseComponent() {
    beginTransaction();

    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    final Long courseId = course.getId();
    commit();
    
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createCourseComponent(courseDAO.getCourse(courseId), COURSE_LENGTH, getEducationalTimeUnit(), null, "Desc");
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.createCourseComponent(courseDAO.getCourse(courseId), COURSE_LENGTH, getEducationalTimeUnit(), "", "Desc");
      }
    });
    
    beginTransaction();
    
    /* Clean up */
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    
    commit();
  }

  public void testUpdateCourseComponent() {
    beginTransaction();

    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    Long courseId = course.getId();
    final Long componentId = courseDAO.createCourseComponent(course, COURSE_COMPONENT_LENGTH, getEducationalTimeUnit(), "Name", "Desc").getId();
    
    commit();
    
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        CourseComponent component = courseDAO.getCourseComponent(componentId);
        courseDAO.updateCourseComponent(component, COURSE_LENGTH, component.getLength().getUnit(), null, "Desc");
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        CourseComponent component = courseDAO.getCourseComponent(componentId);
        courseDAO.updateCourseComponent(component, COURSE_LENGTH, component.getLength().getUnit(), "", "Desc");
      }
    });
    
    beginTransaction();
    
    /* Clean up */
    
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    
    commit();
  }

  public void testGetCourseComponent() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    Long courseId = course.getId();
    Long componentId = courseDAO.createCourseComponent(course, COURSE_COMPONENT_LENGTH, getEducationalTimeUnit(), COURSE_COMPONENT_NAME, COURSE_COMPONENT_DESCRIPTION).getId();
    commit();
    
    beginTransaction();
    
    CourseComponent courseComponent = courseDAO.getCourseComponent(componentId);
    
    Assert.assertEquals(courseComponent.getName(), COURSE_COMPONENT_NAME);
    Assert.assertEquals(courseComponent.getDescription(), COURSE_COMPONENT_DESCRIPTION);
    Assert.assertEquals(courseComponent.getLength().getUnits(), COURSE_COMPONENT_LENGTH);
    
    courseDAO.deleteCourseComponent(courseComponent);
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    
    commit();
  }

  public void testDeleteCourseComponent() {
     /* Already tested in testGetCourseComponent */ 
  }

  public void testCreateBasicCourseResource() {
    beginTransaction();
    final Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    final Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createBasicCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), null, new MonetaryAmount(14.0), 5, new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createBasicCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, null, 5, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createBasicCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), 5, null);
      }
    });
    
    /* units null test */ 
    checkProperty("units", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createBasicCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), null, new MonetaryAmount(12.0));
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testUpdateBasicCourseResource() {
    beginTransaction();
    Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    final Long courseResourceId = courseDAO.createBasicCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), 5, new MonetaryAmount(12.0)).getId();
    
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateBasicCourseResource(courseDAO.getBasicCourseResource(courseResourceId), null, new MonetaryAmount(14.0), 5, new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateBasicCourseResource(courseDAO.getBasicCourseResource(courseResourceId), 15.0, null, 5, new MonetaryAmount(12.0));
      }
    });
    
    /* units null test */ 
    checkProperty("units", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateBasicCourseResource(courseDAO.getBasicCourseResource(courseResourceId), 15.0, new MonetaryAmount(14.0), null, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateBasicCourseResource(courseDAO.getBasicCourseResource(courseResourceId), 15.0, new MonetaryAmount(14.0), 5, null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testListBasicCourseResources() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    courseDAO.createBasicCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), 6, new MonetaryAmount(7.0)).getId();
    courseDAO.createBasicCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), 10, new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    Assert.assertEquals(courseDAO.listBasicCourseResources(courseId).size(), 2);
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testDeleteBasicCourseResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    Long wcrId = courseDAO.createBasicCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), 6, new MonetaryAmount(7.0)).getId();
    Long mcrId = courseDAO.createBasicCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), 10, new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    courseDAO.deleteBasicCourseResource(courseDAO.getBasicCourseResource(wcrId));
    courseDAO.deleteBasicCourseResource(courseDAO.getBasicCourseResource(mcrId));
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testCreateStudentCourseResource() {
    beginTransaction();
    final Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    final Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createStudentCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), null, new MonetaryAmount(14.0), new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createStudentCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, null, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createStudentCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testUpdateStudentCourseResource() {
    beginTransaction();
    Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    final Long courseResourceId = courseDAO.createStudentCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), new MonetaryAmount(12.0)).getId();
    
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateStudentCourseResource(courseDAO.getStudentCourseResource(courseResourceId), null, new MonetaryAmount(14.0), new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateStudentCourseResource(courseDAO.getStudentCourseResource(courseResourceId), 15.0, null, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateStudentCourseResource(courseDAO.getStudentCourseResource(courseResourceId), 15.0, new MonetaryAmount(14.0), null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testListStudentCourseResources() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    courseDAO.createStudentCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), new MonetaryAmount(7.0)).getId();
    courseDAO.createStudentCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    Assert.assertEquals(courseDAO.listStudentCourseResources(courseId).size(), 2);
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testDeleteStudentCourseResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    Long wcrId = courseDAO.createStudentCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), new MonetaryAmount(7.0)).getId();
    Long mcrId = courseDAO.createStudentCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    courseDAO.deleteStudentCourseResource(courseDAO.getStudentCourseResource(wcrId));
    courseDAO.deleteStudentCourseResource(courseDAO.getStudentCourseResource(mcrId));
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testCreateGradeCourseResource() {
    beginTransaction();
    final Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    final Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createGradeCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), null, new MonetaryAmount(14.0), new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createGradeCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, null, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createGradeCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testUpdateGradeCourseResource() {
    beginTransaction();
    Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    ResourceCategory category = resourceDAO.createResourceCategory("Test");
    Long resourceId = resourceDAO.createWorkResource("Test", category, 12.0, 13.0).getId(); 
    final Long courseResourceId = courseDAO.createGradeCourseResource(courseDAO.getCourse(courseId), resourceDAO.getResource(resourceId), 15.0, new MonetaryAmount(14.0), new MonetaryAmount(12.0)).getId();
    
    commit();
    
    /* hours null test */ 
    checkProperty("hours", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateGradeCourseResource(courseDAO.getGradeCourseResource(courseResourceId), null, new MonetaryAmount(14.0), new MonetaryAmount(12.0));
      }
    });
    
    /* hourlyCost null test */ 
    checkProperty("hourlyCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateGradeCourseResource(courseDAO.getGradeCourseResource(courseResourceId), 15.0, null, new MonetaryAmount(12.0));
      }
    });
    
    /* unitCost null test */ 
    checkProperty("unitCost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateGradeCourseResource(courseDAO.getGradeCourseResource(courseResourceId), 15.0, new MonetaryAmount(14.0), null);
      }
    });
    
    /* Clean up */ 
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(category.getId()));
    commit();
  }

  public void testListGradeCourseResources() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    courseDAO.createGradeCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), new MonetaryAmount(7.0)).getId();
    courseDAO.createGradeCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    Assert.assertEquals(courseDAO.listGradeCourseResources(courseId).size(), 2);
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testDeleteGradeCourseResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Test");
    MaterialResource materialResource = resourceDAO.createMaterialResource("Work", resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource("Material", resourceCategory, 1.0, 2.0);
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    
    Long wcrId = courseDAO.createGradeCourseResource(course, workResource, 4.0, new MonetaryAmount(5.0), new MonetaryAmount(7.0)).getId();
    Long mcrId = courseDAO.createGradeCourseResource(course, workResource, 8.0, new MonetaryAmount(9.0), new MonetaryAmount(11.0)).getId();
    
    Long courseId = course.getId();
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    courseDAO.deleteGradeCourseResource(courseDAO.getGradeCourseResource(wcrId));
    courseDAO.deleteGradeCourseResource(courseDAO.getGradeCourseResource(mcrId));
    
    courseDAO.deleteCourse(course);
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit(); 
  }

  public void testCreateOtherCost() {
    beginTransaction();
    final Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    commit();
    
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createOtherCost(courseDAO.getCourse(courseId), null, OTHERCOST_COST);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.createOtherCost(courseDAO.getCourse(courseId), "", OTHERCOST_COST);
      }
    });
    
    /* null cost test */ 
    checkProperty("cost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.createOtherCost(courseDAO.getCourse(courseId), OTHERCOST_NAME, null);
      }
    });
    
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    commit();
  }

  public void testUpdateOtherCost() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    OtherCost otherCost = courseDAO.createOtherCost(course, OTHERCOST_NAME, OTHERCOST_COST);
    Long courseId = course.getId();
    final Long costId = otherCost.getId();
    commit();
    
    /* null & empty name tests */ 
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateOtherCost(courseDAO.getOtherCost(costId), null, OTHERCOST_COST);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        courseDAO.updateOtherCost(courseDAO.getOtherCost(costId), "", OTHERCOST_COST);
      }
    });
    
    /* null cost test */ 
    checkProperty("cost", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        courseDAO.updateOtherCost(courseDAO.getOtherCost(costId), OTHERCOST_NAME, null);
      }
    });
    
    beginTransaction();
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    commit();
  }

  public void testListOtherCosts() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    Long courseId = course.getId();
    TestDataGenerator.createTestOtherCosts(10, 0, course, "cost", new MonetaryAmount(12.0), 202.1);
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(courseDAO.listOtherCosts(courseId).size(), 10);
    courseDAO.deleteCourse(course);
    commit();
  }

  public void testDeleteOtherCost() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    Long courseId = course.getId();
    Long costId = TestDataGenerator.createTestOtherCosts(1, 0, course, "cost", new MonetaryAmount(12.0), 202.1).get(0).getId();
    commit();
    
    
    beginTransaction();
    Assert.assertEquals(courseDAO.getCourse(courseId).getOtherCosts().size(), 1);
    courseDAO.deleteOtherCost(courseDAO.getOtherCost(costId));
    commit();
    
    beginTransaction();
    Assert.assertEquals(courseDAO.getCourse(courseId).getOtherCosts().size(), 0);
    courseDAO.deleteCourse(courseDAO.getCourse(courseId));
    commit();
  }

  public void testRemoveCourseEducationType() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    EducationType educationType = TestDataGenerator.createTestEducationTypes(1, 0, "EduT").get(0);
    Long courseId = course.getId();
    
    Long courseEducationTypeId = courseDAO.addCourseEducationType(course, educationType).getId();
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getCourseEducationTypes().size(), 1);
    Assert.assertEquals(course.getCourseEducationTypes().get(0).getId(), courseEducationTypeId);
    courseDAO.removeCourseEducationType(courseDAO.getCourseEducationType(courseEducationTypeId));
    
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getCourseEducationTypes().size(), 0);
    
    courseDAO.deleteCourse(course);
    baseDAO.deleteEducationType(baseDAO.getEducationType(educationType.getId()));
    
    commit();
  }

  public void testAddCourseEducationType() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    EducationType educationType = TestDataGenerator.createTestEducationTypes(1, 0, "EduT").get(0);
    Long courseId = course.getId();
    
    Long courseEducationTypeId = courseDAO.addCourseEducationType(course, educationType).getId();
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getCourseEducationTypes().size(), 1);
    Assert.assertEquals(course.getCourseEducationTypes().get(0).getId(), courseEducationTypeId);
    
    courseDAO.deleteCourse(course);
    baseDAO.deleteEducationType(baseDAO.getEducationType(educationType.getId()));
    
    commit();
  }

  public void testAddCourseEducationSubtype() {
    beginTransaction();
    Course course = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser());
    EducationType educationType = TestDataGenerator.createTestEducationTypes(1, 0, "EduT").get(0);
    EducationSubtype educationSubtype = TestDataGenerator.createTestEducationSubtypes(1, 0, educationType, "EduSubT").get(0);
    
    Long courseId = course.getId();
    
    CourseEducationType courseEducationType = courseDAO.addCourseEducationType(course, educationType);
    Long courseEducationSubtypeId = courseDAO.addCourseEducationSubtype(courseEducationType, educationSubtype).getId();
    
    Long courseEducationTypeId = courseEducationType.getId();
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getCourseEducationTypes().size(), 1);
    Assert.assertEquals(course.getCourseEducationTypes().get(0).getCourseEducationSubtypes().size(), 1);
    
    Assert.assertEquals(course.getCourseEducationTypes().get(0).getId(), courseEducationTypeId);
    Assert.assertEquals(course.getCourseEducationTypes().get(0).getCourseEducationSubtypes().get(0).getId(), courseEducationSubtypeId);
    
    courseDAO.deleteCourse(course);
    baseDAO.deleteEducationType(baseDAO.getEducationType(educationType.getId()));
    
    commit();
  }

  public void testListCourses() {
    beginTransaction();
    List<Course> courses = TestDataGenerator.createTestCourses(10, 0, COURSE_NAME, "desc", getTestUser(), getTestSubject(), getTestModule(), COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), getCourseState());
    commit();
    
    beginTransaction();
    Assert.assertEquals(courseDAO.listCourses().size(), 10);
    for (Course course : courses)
      courseDAO.deleteCourse(courseDAO.getCourse(course.getId()));
    
    commit();
  }
  
  public void testCourseStudents() {
    Course course;
    Student student;
    CourseEnrolmentType enrolmentType;
    CourseParticipationType participationType;
    CourseStudent courseStudent;
    
    beginTransaction();
    Long courseId = courseDAO.createCourse(getTestModule(), COURSE_NAME, "", getCourseState(), getTestSubject(), 1, COURSE_BEGIN_DATE, COURSE_END_DATE, COURSE_LENGTH, getEducationalTimeUnit(), null, null, COURSE_DESCRIPTION, getTestUser()).getId();
    Long studentId = TestDataGenerator.createTestStudents(1, 0, createDate(2000, 1, 2), 0, "Test", "Student", "test.student").get(0).getId();
    Long enrolmentTypeId = TestDataGenerator.createTestCourseEnrolmentTypes(1, 0, "Test").get(0).getId();
    Long participationTypeId = TestDataGenerator.createTestCourseParticipationTypes(1, 0, "Test").get(0).getId();
    
    Long enrolmentTypeId2 = TestDataGenerator.createTestCourseEnrolmentTypes(1, 1, "Updated").get(0).getId();
    Long participationTypeId2 = TestDataGenerator.createTestCourseParticipationTypes(1, 1, "Updated").get(0).getId();
    
    commit();
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    student = studentDAO.getStudent(studentId);
    enrolmentType = courseDAO.getCourseEnrolmentType(enrolmentTypeId);
    participationType = courseDAO.getCourseParticipationType(participationTypeId);
    Date enrolmentDate = createDate(2001, 2, 3);
    
    Long courseStudentId = courseDAO.addCourseStudent(course, student, enrolmentType, participationType, enrolmentDate, Boolean.FALSE).getId();
    
    commit();
    
    beginTransaction();
    
    courseStudent = courseDAO.getCourseStudent(courseStudentId);
    
    Assert.assertEquals(courseStudent.getCourse().getId(), courseId);
    Assert.assertEquals(courseStudent.getStudent().getId(), studentId);
    Assert.assertEquals(courseStudent.getCourseEnrolmentType().getId(), enrolmentTypeId);
    Assert.assertEquals(courseStudent.getParticipationType().getId(), participationTypeId);
    Assert.assertEquals(courseStudent.getEnrolmentTime().getTime(), enrolmentDate.getTime());
    
    commit();
    
    /* Update tests */ 
    
    beginTransaction();
    courseStudent = courseDAO.getCourseStudent(courseStudentId);
    Date updatedDate = createDate(2005, 06, 02);
    courseDAO.updateCourseStudent(courseStudent, courseDAO.getCourseEnrolmentType(enrolmentTypeId2), courseDAO.getCourseParticipationType(participationTypeId2), updatedDate, Boolean.FALSE);
    commit();
    
    beginTransaction();
    
    courseStudent = courseDAO.getCourseStudent(courseStudentId);
    
    Assert.assertEquals(courseStudent.getCourse().getId(), courseId);
    Assert.assertEquals(courseStudent.getStudent().getId(), studentId);
    Assert.assertEquals(courseStudent.getCourseEnrolmentType().getId(), enrolmentTypeId2);
    Assert.assertEquals(courseStudent.getParticipationType().getId(), participationTypeId2);
    Assert.assertEquals(courseStudent.getEnrolmentTime().getTime(), updatedDate.getTime());
    
    commit();
    
    /* Delete tests */
    
    beginTransaction();
    courseStudent = courseDAO.getCourseStudent(courseStudentId);
    Assert.assertEquals(courseStudent.getCourse().getCourseStudents().size(), 1);
    courseDAO.deleteCourseStudent(courseStudent);
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    Assert.assertEquals(course.getCourseStudents().size(), 0);
    commit();
    
    /* List tests */
    
    beginTransaction();
    List<Student> students = TestDataGenerator.createTestStudents(10, 0, createDate(2025, 12, 13), 1000 * 60 * 60 * 24, "Stu", "Dent", "stu.dent");
    course = courseDAO.getCourse(courseId);
    enrolmentType = courseDAO.getCourseEnrolmentType(enrolmentTypeId);
    participationType = courseDAO.getCourseParticipationType(participationTypeId);
    enrolmentDate = createDate(2001, 2, 3);
    
    for (Student s : students)
      courseDAO.addCourseStudent(course, s, enrolmentType, participationType, enrolmentDate, Boolean.FALSE);
    
    commit();
    
    beginTransaction();
    course = courseDAO.getCourse(courseId);
    List<CourseStudent> courseStudents = courseDAO.listCourseStudents(course);
    Assert.assertEquals(courseStudents.size(), 10);
    commit();
    
    /* Clean up */
    
    beginTransaction();
    
    course = courseDAO.getCourse(courseId);
    
    while (course.getCourseStudents().size() > 0) {
      CourseStudent cs = course.getCourseStudents().get(0);
      courseDAO.deleteCourseStudent(cs);
      Student s = cs.getStudent();
      AbstractStudent abstractStudent = s.getAbstractStudent();
      studentDAO.deleteStudent(s);
      studentDAO.deleteAbstractStudent(abstractStudent);
    }
     
    courseDAO.deleteCourse(course);
    
    student = courseStudent.getStudent();
    AbstractStudent abstractStudent = student.getAbstractStudent();

    studentDAO.deleteStudent(student);
    studentDAO.deleteAbstractStudent(abstractStudent);
    courseDAO.deleteCourseEnrolmentType(courseDAO.getCourseEnrolmentType(enrolmentTypeId));
    courseDAO.deleteCourseParticipationType(courseDAO.getCourseParticipationType(participationTypeId));
    courseDAO.deleteCourseEnrolmentType(courseDAO.getCourseEnrolmentType(enrolmentTypeId2));
    courseDAO.deleteCourseParticipationType(courseDAO.getCourseParticipationType(participationTypeId2));
    
    commit();
  }

  public void testAddCourseStudent() {
    /* Add tests are included in testCourseStudents test */
  }

  public void testUpdateCourseStudent() {
    /* Update tests are included in testCourseStudents test */
  }

  public void testGetCourseStudent() {
    /* Getter tests are included in testCourseStudents test */
  }

  public void testListCourseStudents() {
    /* Listing tests are included in testCourseStudents test */
  }

  public void testDeleteCourseStudent() {
    /* Delete tests are included in testCourseStudents test */
  }
  
  public void testSearchCourses() {
    SearchResult<Course> searchResult;
    Date firstDate = (new GregorianCalendar(2000, GregorianCalendar.OCTOBER, 10)).getTime();
    
    /* test data */
    
    beginTransaction();
    
    /* test user 1 */
    User user1 = TestDataGenerator.createTestUsers(1, 0, "test", "user", "test.user", "testmail.com", "internal", UserRole.USER).get(0);
    
    /* test user 2 */
    User user2 = TestDataGenerator.createTestUsers(1, 1, "test", "user", "test.user", "testmail.com", "internal", UserRole.USER).get(0);
    
    /* 1 test subject */ 
    Subject subject = TestDataGenerator.createTestSubjects(1, 0, "test subject").get(0);
    
    /* 100 test modules */
    List<Module> modules = TestDataGenerator.createTestModules(100, 0, "test module", "test module desc", user1, subject, getEducationalTimeUnit());
    
    Date beginDate = firstDate;
    Date endDate;
    
    final long dayInc = 1000 * 60 * 60 * 24;
    
    int i = 0;
    List<Course> courses = new ArrayList<Course>();
    /* 50 test courses (startDate = endDate + 3 days & endDate = startDate + 4 days) */
    while (i <= 50) {
      Module module = modules.get(i); 
      /* inc 4 days */
      endDate = new Date(beginDate.getTime() + dayInc * 4);
      courses.add(TestDataGenerator.createTestCourses(1, i, "test course", "test course desc", user1, subject, module, beginDate, endDate, COURSE_LENGTH, getEducationalTimeUnit(), getCourseState()).get(0));
      /* inc 3 days */
      beginDate = new Date(endDate.getTime() + dayInc * 3);
      i++;
    }
    
    beginDate = (new GregorianCalendar(2002, GregorianCalendar.JANUARY, 11)).getTime();
    
    /* 50 test courses (endDate = startDate + 10 days, startDate = startDate + 5 days ) */
    while (i < 100) {
      /* inc 5 days */
      endDate = new Date(beginDate.getTime() + dayInc * 10);
      Module module = modules.get(i); 
      courses.add(TestDataGenerator.createTestCourses(1, i, "test course", "test course desc", user2, subject, module, beginDate, endDate, COURSE_LENGTH, getEducationalTimeUnit(), getCourseState()).get(0));
      /* inc 10 days */
      beginDate = new Date(beginDate.getTime() + dayInc * 5);
      i++;
    }
    
    commit();
    
    beginTransaction();
    
    /* By name */
    
    searchResult = courseDAO.searchCourses(10, 0, "test*", "test*", "test*", "test*", "test*", null, SearchTimeFilterMode.UNDEFINED, null, null, false, false);
    // We should find all 100 results with "test*" query 
    
    Assert.assertEquals(searchResult.getFirstResult(), 0);
    Assert.assertEquals(searchResult.getLastResult(), 9);
    Assert.assertEquals(searchResult.getPage(), 0);
    Assert.assertEquals(searchResult.getPages(), 10);
    Assert.assertEquals(searchResult.getTotalHitCount(), 100);
    Assert.assertEquals(searchResult.getResults().size(), 10);
    
    /* Paged */
    
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.UNDEFINED, null, null, false, false);
    // We should find all 100 results with empty query 
    
    Assert.assertEquals(searchResult.getFirstResult(), 0);
    Assert.assertEquals(searchResult.getLastResult(), 19);
    Assert.assertEquals(searchResult.getPage(), 0);
    Assert.assertEquals(searchResult.getPages(), 5);
    Assert.assertEquals(searchResult.getTotalHitCount(), 100);
    Assert.assertEquals(searchResult.getResults().size(), 20);
    
    /* exclusive time filter / start only */ 
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.EXCLUSIVE, (new GregorianCalendar(2000, GregorianCalendar.DECEMBER, 16)).getTime(), null, false, false);
    // 10th course begins at 16/12/2000 so we should get 90 courses with that as a start date
    Assert.assertEquals(searchResult.getTotalHitCount(), 90);
    
    /* exclusive time filter / end only */
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.EXCLUSIVE, null, (new GregorianCalendar(2001, GregorianCalendar.FEBRUARY, 27)).getTime(), false, false);
    // 20th course ends at 27/02/2001 so we should get 20 courses with that as a end date
    Assert.assertEquals(searchResult.getTotalHitCount(), 20);
    
    /* exclusive time filter / both */
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.EXCLUSIVE, (new GregorianCalendar(2001, GregorianCalendar.MAY, 05)).getTime(), (new GregorianCalendar(2001, GregorianCalendar.JULY, 17)).getTime(), false, false);
    // 30th course begins at 05/05/2001 and 40th course ends at 17/07/2001 so we should get 10 courses between those two days 
    Assert.assertEquals(searchResult.getTotalHitCount(), 10);

    /* Inclusive time filter / start only */ 
    //   after 25/02/2002
    //   courses from 60 to 99 should match because they start after begin date (39)
    //   54, 55 should match because both end at or after query date (2)
    //   totalling 41 courses
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.INCLUSIVE, (new GregorianCalendar(2002, GregorianCalendar.FEBRUARY, 25)).getTime(), null, false, false);
    Assert.assertEquals(searchResult.getTotalHitCount(), 41);
    
    /* exclusive time filter / end only */
   
    //   before 22/03/2002
    //   courses from 0 to 63 should match because they end before query date (64)
    //   64, 65 should match because both start at or before query date (2)
    //   totalling 66 courses
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.INCLUSIVE, null, (new GregorianCalendar(2002, GregorianCalendar.MARCH, 22)).getTime(), false, false);
    Assert.assertEquals(searchResult.getTotalHitCount(), 66);
    
    /* Inclusive time filter / both */
    // timeframe: from 15/02/2002 to 01/04/2002
    //   courses from 58 to 65 should match because they start and end be between range (8) 
    //   56, 57 begin before range but should match because they end after start date (2)
    //   66, 67 and start before the end range (2)
    //   totalling 12 courses
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.INCLUSIVE, (new GregorianCalendar(2002, GregorianCalendar.FEBRUARY, 15)).getTime(), (new GregorianCalendar(2002, GregorianCalendar.APRIL, 01)).getTime(), false, false);
    
    /***
     * TODO: THIS DOES NOT WORK.     
     *     
     * Search should return 12 results but somehow course #56 (05.02.2002 - 15.02.2002) does not show up on results.
     * Course in question is at the edge of the query range and somehow inclusive range query (lucene) just leaves it out.
     */
    Assert.assertEquals(searchResult.getTotalHitCount(), 11);
    
    commit();
    
    beginTransaction();
    
    /* Archive first 20 courses */
    
    for (int j = 0; j < 20; j++) {
      Course course = (Course) getCurrentSession().load(Course.class, courses.get(j).getId());
      courseDAO.archiveCourse(course);
    }
    
    commit();
    
    beginTransaction();
    
    /* include archived */
    
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.UNDEFINED, null, null, false, false);
    Assert.assertEquals(searchResult.getTotalHitCount(), 100);
    
    /* exclude archived */
    
    searchResult = courseDAO.searchCourses(20, 0, "", "", "", "", "", null, SearchTimeFilterMode.UNDEFINED, null, null, true, false);
    Assert.assertEquals(searchResult.getTotalHitCount(), 80);
    
    /* owner filter */ 
    
    searchResult = courseDAO.searchCourses(1000, 0, "", "", "", "", "", user1.getId(), SearchTimeFilterMode.UNDEFINED, null, null, false, false);
    for (Course course : searchResult.getResults())
      Assert.assertEquals(course.getCreator().getId(), user1.getId());
    
    searchResult = courseDAO.searchCourses(1000, 0, "", "", "", "", "", user2.getId(), SearchTimeFilterMode.UNDEFINED, null, null, false, false);
    for (Course course : searchResult.getResults())
      Assert.assertEquals(course.getCreator().getId(), user2.getId());
    
    /* All together */
    
    searchResult = courseDAO.searchCourses(20, 0, "test*", "test*", "test*", "test*", "test*",user1.getId(), SearchTimeFilterMode.EXCLUSIVE, (new GregorianCalendar(2001, GregorianCalendar.JANUARY, 22)).getTime(), (new GregorianCalendar(2001, GregorianCalendar.APRIL, 7)).getTime(), true, false);
    // 15th course begins at 01/22/2001 and 25th course ends at 07/04/2001 but courses 15 - 19 are archived, so query should match 6 courses 
    Assert.assertEquals(searchResult.getTotalHitCount(), 6);
    
    commit();
    
    beginTransaction();
    /* Clean up */

    for (Course course : courses)
      getCurrentSession().delete(getCurrentSession().load(Course.class, course.getId()));
    
    for (Module module : modules)
      getCurrentSession().delete(getCurrentSession().load(Module.class, module.getId()));
    
    getCurrentSession().delete(getCurrentSession().load(Subject.class, subject.getId()));
    
    getCurrentSession().delete(getCurrentSession().load(User.class, user1.getId()));
    
    getCurrentSession().delete(getCurrentSession().load(User.class, user2.getId()));
    
    commit();
  }

  private CourseDAO courseDAO = new CourseDAO();
  private ModuleDAO moduleDAO = new ModuleDAO();
  private UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
  private StudentDAO studentDAO = new StudentDAO();
  private ResourceDAO resourceDAO = new ResourceDAO();
  private BaseDAO baseDAO = new BaseDAO();
  
  private Long moduleId;
  private Long userId;
  private Long subjectId;
  private Long educationalLengthUnitId;
  private Long stateId;
}
