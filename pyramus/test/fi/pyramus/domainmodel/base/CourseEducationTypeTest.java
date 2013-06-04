package fi.pyramus.domainmodel.base;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;


@Test
public class CourseEducationTypeTest extends DatabaseDependingTest {

  @BeforeClass
  protected void setUp() throws Exception {
    
    beginTransaction();

    // User...
    
    User user = new User();
    user.setAuthProvider("internal");
    user.setExternalId("-1");
    user.setFirstName("User");
    user.setLastName("Pass");
    user.setRole(Role.USER);
    getCurrentSession().save(user);
    userId = user.getId();
    
    // ...a couple of modules...
    
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit();
    educationalTimeUnit.setBaseUnits(1.0);
    educationalTimeUnit.setName("nothing");
    getCurrentSession().saveOrUpdate(educationalTimeUnit);
    
    EducationalLength moduleLength1 = new EducationalLength();
    moduleLength1.setUnit(educationalTimeUnit);
    moduleLength1.setUnits(32.0);

    EducationalLength moduleLength2 = new EducationalLength();
    moduleLength2.setUnit(educationalTimeUnit);
    moduleLength2.setUnits(32.0);
    
    Module module = new Module();
    module.setCourseLength(moduleLength1);
    module.setName("Module #1");
    module.setCreated(new Date());
    module.setCreator(user);
    module.setLastModified(new Date());
    module.setLastModifier(user);
    getCurrentSession().save(module);
    module1Id = module.getId();
    
    module = new Module();
    module.setCourseLength(moduleLength2);
    module.setName("Module #2");
    module.setCreated(new Date());
    module.setCreator(user);
    module.setLastModified(new Date());
    module.setLastModifier(user);
    getCurrentSession().save(module);
    module2Id = module.getId();
    
    // ...some education types...
    
    EducationType educationType = new EducationType();
    educationType.setName("Education type #1");
    getCurrentSession().save(educationType);
    educationType1Id = educationType.getId();
    
    educationType = new EducationType();
    educationType.setName("Education type #2");
    getCurrentSession().save(educationType);
    educationType2Id = educationType.getId();
    
    // ...and some subtypes as well

    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Subtype #1");
    getCurrentSession().save(educationSubtype);
    educationSubtype11Id = educationSubtype.getId();
    educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Subtype #2");
    getCurrentSession().save(educationSubtype);
    educationSubtype12Id = educationSubtype.getId();
    
    commit();
  }

  @AfterClass
  protected void tearDown() throws Exception {
    beginTransaction();
   
    Module module1 = (Module) getCurrentSession().load(Module.class, module1Id);
    Module module2 = (Module) getCurrentSession().load(Module.class, module2Id);
    
    EducationalLength educationalLength1 = module1.getCourseLength();
    EducationalLength educationalLength2 = module2.getCourseLength();
    EducationalTimeUnit educationalTimeUnit = educationalLength2.getUnit();
    
    getCurrentSession().delete(module1);
    getCurrentSession().delete(module2);
    getCurrentSession().delete(getCurrentSession().load(EducationType.class, educationType1Id));
    getCurrentSession().delete(getCurrentSession().load(EducationType.class, educationType2Id));
    getCurrentSession().delete(getCurrentSession().load(User.class, userId));
    getCurrentSession().delete(educationalLength1);
    getCurrentSession().delete(educationalLength2);
    getCurrentSession().delete(educationalTimeUnit);
    
    commit();
  }

  @Test
  public void testGetId() {
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, module1Id);
    EducationType educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
    CourseEducationType courseEducationType = new CourseEducationType(educationType);
    module.addCourseEducationType(courseEducationType);
    Assert.assertNull(courseEducationType.getId());
    getCurrentSession().saveOrUpdate(module);
    commit();
    
    Assert.assertNotNull(courseEducationType.getId());
  }

  @Test
  public void testGetModule() {
    Module module;
    EducationType educationType;
    CourseEducationType courseEducationType;
    Long moduleEducationTypeId;
    
    // Add an education type to module #1...
    
    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, module1Id);
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
    courseEducationType = new CourseEducationType(educationType);
    module.addCourseEducationType(courseEducationType);
    getCurrentSession().saveOrUpdate(module);
    commit();
    moduleEducationTypeId = courseEducationType.getId();
    
    // ...see that it sticks...

    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, module1Id);
    courseEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
    Assert.assertEquals(courseEducationType.getCourseBase().getId(), module.getId());
    commit();
  }

  @Test
  public void testGetEducationType() {
    Module module;
    EducationType educationType;
    CourseEducationType moduleEducationType;
    Long moduleEducationTypeId;

    // Set the education type...
    
    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, module1Id);
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
    moduleEducationType = new CourseEducationType(educationType);
    module.addCourseEducationType(moduleEducationType);
    getCurrentSession().saveOrUpdate(module);
    commit();
    moduleEducationTypeId = moduleEducationType.getId();
    
    // ...and see that it sticks
    
    beginTransaction();
    moduleEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
    Assert.assertEquals(moduleEducationType.getEducationType(), educationType);
    commit();
  }

  @Test
      public void testGetCourseEducationSubtypes() {
        Module module;
        EducationType educationType;
        EducationSubtype educationSubtype;
        CourseEducationType courseEducationType;
        Long moduleEducationTypeId;
    
        // Create the education type...
        
        beginTransaction();
        module = (Module) getCurrentSession().load(Module.class, module1Id);
        educationType = (EducationType) getCurrentSession().load(EducationType.class, educationType1Id);
        courseEducationType = new CourseEducationType(educationType);
        module.addCourseEducationType(courseEducationType);
        getCurrentSession().saveOrUpdate(module);
        commit();
        moduleEducationTypeId = courseEducationType.getId();
        
        // ...add a bunch of subtypes...
        
        beginTransaction();
        courseEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
        educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtype11Id);
        courseEducationType.addSubtype(new CourseEducationSubtype(educationSubtype));
        educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtype12Id);
        courseEducationType.addSubtype(new CourseEducationSubtype(educationSubtype));
        getCurrentSession().saveOrUpdate(courseEducationType);
        commit();
        
        // ...see that we have them...
        
        beginTransaction();
        courseEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
        Assert.assertEquals(courseEducationType.getCourseEducationSubtypes().size(), 2);
        commit();
    
        // ...remove one of them...
        
        beginTransaction();
        courseEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
        courseEducationType.removeSubtype(courseEducationType.getCourseEducationSubtypes().get(0));
        getCurrentSession().saveOrUpdate(courseEducationType);
        commit();
        
        // ...and we should be left with just one
        
        beginTransaction();
        courseEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
        Assert.assertEquals(courseEducationType.getCourseEducationSubtypes().size(), 1);
        commit();
      }

  @Test
  public void testAddSubtype() {
    // TODO Tested in testGetSubtypes
  }

  @Test
  public void testRemoveSubtype() {
    // TODO Tested in testGetSubtypes
  }
  
  private Long userId;
  private Long module1Id;
  private Long module2Id;
  private Long educationType1Id;
  private Long educationType2Id;
  private Long educationSubtype11Id;
  private Long educationSubtype12Id;

}
