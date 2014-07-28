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
public class CourseEducationSubtypeTest extends DatabaseDependingTest {

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
    getCurrentSession().saveOrUpdate(user);
    userId = user.getId();
    
    
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit();
    educationalTimeUnit.setBaseUnits(1.0);
    educationalTimeUnit.setName("nothing");
    getCurrentSession().saveOrUpdate(educationalTimeUnit);
    
    EducationalLength moduleLength = new EducationalLength();
    moduleLength.setUnit(educationalTimeUnit);
    moduleLength.setUnits(32.0);
    
    // ...a module...
    
    Module module = new Module();
    module.setCourseLength(moduleLength);
    module.setName("Module");
    module.setCreated(new Date());
    module.setCreator(user);
    module.setLastModified(new Date());
    module.setLastModifier(user);
    getCurrentSession().saveOrUpdate(module);
    moduleId = module.getId();
    
    // ...an education type...
    
    EducationType educationType = new EducationType();
    educationType.setName("Education type");
    getCurrentSession().saveOrUpdate(educationType);
    educationTypeId = educationType.getId();
    
    // ...an education subtype...

    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Education subtype");
    getCurrentSession().saveOrUpdate(educationSubtype);
    educationSubtypeId = educationSubtype.getId();

    // ...and a module specific instance of the education type

    CourseEducationType courseEducationType = new CourseEducationType(educationType);
    module.addCourseEducationType(courseEducationType);
    getCurrentSession().saveOrUpdate(courseEducationType);
    moduleEducationTypeId = courseEducationType.getId();
    
    commit();
  }

  @AfterClass
  protected void tearDown() throws Exception {
    beginTransaction();
    
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    
    EducationalLength educationalLength = module.getCourseLength();  
    EducationalTimeUnit educationalTimeUnit = educationalLength.getUnit();
    
    getCurrentSession().delete(getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId));
    getCurrentSession().delete(getCurrentSession().load(EducationSubtype.class, educationSubtypeId));
    getCurrentSession().delete(getCurrentSession().load(EducationType.class, educationTypeId));
    getCurrentSession().delete(module);
    getCurrentSession().delete(getCurrentSession().load(User.class, userId));
    getCurrentSession().delete(educationalLength);
    getCurrentSession().delete(educationalTimeUnit);
    
    commit();
  }

  @Test
  public void testGetId() {
    beginTransaction();

    EducationSubtype educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
    CourseEducationType moduleEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
    
    CourseEducationSubtype moduleEducationSubtype = new CourseEducationSubtype(educationSubtype);
    moduleEducationType.addSubtype(moduleEducationSubtype);
    Assert.assertNull(moduleEducationSubtype.getId());
    getCurrentSession().saveOrUpdate(moduleEducationType);
    
    commit();
    
    Assert.assertNotNull(moduleEducationSubtype.getId());
  }

  @Test
    public void testGetCourseEducationType() {
      beginTransaction();
  
      EducationSubtype educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
      CourseEducationType moduleEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
      
      CourseEducationSubtype moduleEducationSubtype = new CourseEducationSubtype(educationSubtype);
      moduleEducationType.addSubtype(moduleEducationSubtype);
      getCurrentSession().saveOrUpdate(moduleEducationType);
      
      commit();
      
      Long moduleEducationSubtypeId = moduleEducationSubtype.getId();
      
      beginTransaction();
      moduleEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
      moduleEducationSubtype = (CourseEducationSubtype) getCurrentSession().load(CourseEducationSubtype.class, moduleEducationSubtypeId);
      Assert.assertEquals(moduleEducationSubtype.getCourseEducationType(), moduleEducationType);
      commit();
    }

  @Test
  public void testGetEducationSubtype() {
    beginTransaction();

    EducationSubtype educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
    CourseEducationType moduleEducationType = (CourseEducationType) getCurrentSession().load(CourseEducationType.class, moduleEducationTypeId);
    
    CourseEducationSubtype moduleEducationSubtype = new CourseEducationSubtype(educationSubtype);
    moduleEducationType.addSubtype(moduleEducationSubtype);
    getCurrentSession().saveOrUpdate(moduleEducationType);
    
    commit();
    
    Long moduleEducationSubtypeId = moduleEducationSubtype.getId();
    
    beginTransaction();
    educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
    moduleEducationSubtype = (CourseEducationSubtype) getCurrentSession().load(CourseEducationSubtype.class, moduleEducationSubtypeId);
    Assert.assertEquals(moduleEducationSubtype.getEducationSubtype(), educationSubtype);
    commit();
  }

  private Long userId;
  private Long moduleId;
  private Long educationTypeId;
  private Long educationSubtypeId;
  private Long moduleEducationTypeId;

}
