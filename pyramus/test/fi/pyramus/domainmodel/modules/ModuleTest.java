package fi.pyramus.domainmodel.modules;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;


@Test
public class ModuleTest extends DatabaseDependingTest {
  
  @BeforeMethod
  protected void setUp() throws Exception {
    testStarted = new Date(System.currentTimeMillis());
    beginTransaction();
    
    User user = TestDataGenerator.createTestUsers(1, 0, "Test", "User", "test.user", "snailmail.fi", "internal", UserRole.USER).get(0);
    
    Date now = new Date(System.currentTimeMillis() + 1);
    Module module = new Module();
    
    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit();
    educationalTimeUnit.setName("nothing");
    educationalTimeUnit.setBaseUnits(1.0);
    getCurrentSession().save(educationalTimeUnit);
    
    EducationalLength moduleLength = new EducationalLength();
    moduleLength.setUnit(educationalTimeUnit);
    moduleLength.setUnits(32.0);
    getCurrentSession().save(moduleLength);
    
    this.educationalTimeUnitId = educationalTimeUnit.getId();
    
    module.setCourseLength(moduleLength);
    module.setName("module");
    module.setCreator(user);
    module.setLastModifier(user);
    module.setCreated(now);
    module.setLastModified(now);
    getCurrentSession().save(module);
    
    Subject subject = new Subject();
    subject.setName("Name");
    subject.setCode("1001");
    getCurrentSession().save(subject);
    
    commit();
    
    userId = user.getId();
    moduleId = module.getId();
    subjectId = subject.getId();
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    beginTransaction();

    getCurrentSession().delete(getCurrentSession().load(Subject.class, subjectId));
    getCurrentSession().delete(getCurrentSession().load(Module.class, moduleId));
    getCurrentSession().delete(getCurrentSession().load(User.class, userId));
    getCurrentSession().delete(getCurrentSession().load(EducationalTimeUnit.class, educationalTimeUnitId));
    
    commit();
  }
  
  @Test
  public void testGetId() {
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals(moduleId, module.getId());
    commit();
  }

  @Test
  public void testSetName() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    module.setName("newName");
    getCurrentSession().save(module);
    commit();

    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals("newName", module.getName());
    commit();
  }

  @Test
  public void testGetCreator() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals(userId, module.getCreator().getId());
    commit();
  }

  @Test
  public void testSetCreator() { 
    // TODO: If we can turn creator into a automaticly updated property this test will be useless
  }

  @Test
  public void testGetCreated() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertNotNull(module.getCreated());
    
    // Created should be after test start but before now 
     
    Assert.assertTrue(module.getCreated().getTime() >= testStarted.getTime());
    Assert.assertTrue(module.getCreated().getTime() <= System.currentTimeMillis());
    
    commit();
  }

  @Test
  public void testGetLastModifier() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals(userId, module.getLastModifier().getId());
    commit();
  }

  @Test
  public void testSetLastModifier() throws UnsupportedEncodingException, NoSuchAlgorithmException { 
    beginTransaction();
    
    User user2 = TestDataGenerator.createTestUsers(1, 1, "Test", "User", "test.user", "snailmail.fi", "internal", UserRole.USER).get(0);
    
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    module.setLastModifier(user2);
    commit();
    
    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals(user2.getId(), module.getLastModifier().getId());
    module.setLastModifier((User) getCurrentSession().load(User.class, userId));    
    getCurrentSession().save(module);
    getCurrentSession().delete(getCurrentSession().load(User.class, user2.getId()));
    commit();
  }

  @Test
  public void testGetLastModified() { 
    Date now = new Date(System.currentTimeMillis());
    
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    module.setLastModified(now);
    commit();
    
    beginTransaction();
    
    module = (Module) getCurrentSession().load(Module.class, moduleId);
    
    // Modified should be after test start but before now 
    
    Assert.assertEquals(now.getTime(), module.getLastModified().getTime());
    
    commit();
  }

  @Test
  public void testSetDescription() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    module.setDescription("newDesc");
    getCurrentSession().save(module);
    commit();

    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals("newDesc", module.getDescription());
    commit();
  }

  @Test
  public void testSetSubject() { 
    beginTransaction();
    Module module = (Module) getCurrentSession().load(Module.class, moduleId);
    module.setSubject((Subject) getCurrentSession().load(Subject.class, subjectId));
    getCurrentSession().save(module);
    commit();

    beginTransaction();
    module = (Module) getCurrentSession().load(Module.class, moduleId);
    Assert.assertEquals(subjectId, module.getSubject().getId());
    commit();
  }

  @Test
      public void testGetModuleComponents() { 
        beginTransaction();
        Module module = (Module) getCurrentSession().load(Module.class, moduleId);
        
        EducationalTimeUnit educationalTimeUnit = (EducationalTimeUnit) getCurrentSession().load(EducationalTimeUnit.class, educationalTimeUnitId);
        
        EducationalLength objective1Length = new EducationalLength();
        objective1Length.setUnits(1.0);
        objective1Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective1Length);
        
        ModuleComponent objective1 = new ModuleComponent();
        objective1.setName("Name #1");
        objective1.setDescription("Desc #1");
        objective1.setLength(objective1Length);
        module.addModuleComponent(objective1);
        
        EducationalLength objective2Length = new EducationalLength();
        objective2Length.setUnits(2.0);
        objective2Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective2Length);
       
        ModuleComponent objective2 = new ModuleComponent();
        objective2.setName("Name #2");
        objective2.setDescription("Desc #2");
        objective2.setLength(objective2Length);
        objective2.setModule(module);
        module.addModuleComponent(objective2);
        
        EducationalLength objective3Length = new EducationalLength();
        objective3Length.setUnits(3.0);
        objective3Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective3Length);
        
        ModuleComponent objective3 = new ModuleComponent();
        objective3.setName("Name #3");
        objective3.setDescription("Desc #3");
        objective3.setLength(objective3Length);
        objective3.setModule(module);
        module.addModuleComponent(objective3);
        
        getCurrentSession().save(module);
        commit();
    
        beginTransaction();
        module = (Module) getCurrentSession().load(Module.class, moduleId);
        Assert.assertEquals(module.getModuleComponents().size(), 3);
        module.getModuleComponents().clear();
        getCurrentSession().saveOrUpdate(module);
        
        
        commit();
      }

  @Test
      public void testRemoveModuleComponent() { 
        beginTransaction();
        Module module = (Module) getCurrentSession().load(Module.class, moduleId);
        
        EducationalTimeUnit educationalTimeUnit = (EducationalTimeUnit) getCurrentSession().load(EducationalTimeUnit.class, educationalTimeUnitId);
        
        EducationalLength objective1Length = new EducationalLength();
        objective1Length.setUnits(1.0);
        objective1Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective1Length);
        
        ModuleComponent objective1 = new ModuleComponent();
        objective1.setName("Name #1");
        objective1.setDescription("Desc #1");
        objective1.setLength(objective1Length);
        module.addModuleComponent(objective1);
        
        EducationalLength objective2Length = new EducationalLength();
        objective2Length.setUnits(2.0);
        objective2Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective2Length);
       
        ModuleComponent objective2 = new ModuleComponent();
        objective2.setName("Name #2");
        objective2.setDescription("Desc #2");
        objective2.setLength(objective2Length);
        objective2.setModule(module);
        module.addModuleComponent(objective2);
        
        EducationalLength objective3Length = new EducationalLength();
        objective3Length.setUnits(3.0);
        objective3Length.setUnit(educationalTimeUnit);
        getCurrentSession().saveOrUpdate(objective3Length);
        
        ModuleComponent objective3 = new ModuleComponent();
        objective3.setName("Name #3");
        objective3.setDescription("Desc #3");
        objective3.setLength(objective3Length);
        objective3.setModule(module);
        module.addModuleComponent(objective3);
        getCurrentSession().save(module);
        commit();
        
        beginTransaction();
        module = (Module) getCurrentSession().load(Module.class, moduleId);
        Assert.assertEquals(module.getModuleComponents().size(), 3);
        module.removeModuleComponent((ModuleComponent) getCurrentSession().load(ModuleComponent.class, objective3.getId()));
        getCurrentSession().saveOrUpdate(module);
        commit();
    
        beginTransaction();
        module = (Module) getCurrentSession().load(Module.class, moduleId);
        Assert.assertEquals(module.getModuleComponents().size(), 2);
        module.getModuleComponents().clear();
        getCurrentSession().saveOrUpdate(module);
        commit();
        
      }
  
  private Date testStarted;
  private Long moduleId;
  private Long userId;
  private Long subjectId;
  private Long educationalTimeUnitId;
}
