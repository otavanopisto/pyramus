package fi.pyramus.dao;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;



@Test 
public class ModuleDAOTest extends DatabaseDependingTest {

  private static final String       MODULE_NAME                  = "name / 名字";
  private static final String       MODULE_DESCRIPTION           = "description / 描述";
  private static final Double       MODULE_LENGTH                = 15.5;

  private static final String       MODULE_COMPONENT_NAME        = "Module name / اسم وحدة";
  private static final String       MODULE_COMPONENT_DESCRIPTION = "Module description / وصف وحدة";
  private static final Double       MODULE_COMPONENT_LENGTH      = 3.0;

  @BeforeClass
  public void setUp() {
    beginTransaction();
    
    
    User user = userDAO.createUser("Test", "User", "-5", "internal", UserRole.USER);
  
    this.userId = user.getId();
    
    commit();
  }
    
  @AfterClass 
  public void tearDown() {
    beginTransaction();

    userDAO.deleteUser(userDAO.getUser(userId));
    
    commit();
  }
  
  private User getTestUser() {
    return userDAO.getUser(userId);
  }

  @Test
  public void testCreateModuleComponent() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    Long subjectId = subject.getId();
    
    EducationalTimeUnit moduleTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    
    Module module = moduleDAO.createModule(MODULE_NAME, baseDAO.getSubject(subjectId), 1, MODULE_LENGTH, moduleTimeUnit, MODULE_DESCRIPTION, getTestUser());
    final Long moduleId = module.getId(); 
    
    commit();
    
    /* name null & empty tests */
    
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        EducationalTimeUnit componentTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
        moduleDAO.createModuleComponent(moduleDAO.getModule(moduleId), MODULE_COMPONENT_LENGTH, componentTimeUnit, null, MODULE_COMPONENT_DESCRIPTION);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        EducationalTimeUnit componentTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
        moduleDAO.createModuleComponent(moduleDAO.getModule(moduleId), MODULE_COMPONENT_LENGTH, componentTimeUnit, "", MODULE_COMPONENT_DESCRIPTION);
      }
    });
    
    beginTransaction();
    
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(moduleTimeUnit.getId()));
    
    commit();
  }

  @Test
  public void testDeleteModuleComponent() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    Long subjectId = subject.getId();
    EducationalTimeUnit moduleTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    Module module = moduleDAO.createModule(MODULE_NAME, baseDAO.getSubject(subjectId), 1, MODULE_LENGTH, moduleTimeUnit, MODULE_DESCRIPTION, getTestUser());
    Long moduleId = module.getId();   
    Long moduleComponentId = moduleDAO.createModuleComponent(module, 16.75, moduleTimeUnit, "Test", "Desc").getId();
    
    commit();
    
    beginTransaction();
    
    moduleDAO.deleteModuleComponent(moduleDAO.getModuleComponent(moduleComponentId));
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(moduleTimeUnit.getId()));
    
    commit();
  }
  
  @Test
  public void testGetModuleComponent() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    Long subjectId = subject.getId();
    EducationalTimeUnit moduleTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    
    Module module = moduleDAO.createModule(MODULE_NAME, baseDAO.getSubject(subjectId), 1, MODULE_LENGTH, moduleTimeUnit, MODULE_DESCRIPTION, getTestUser());
    Long moduleId = module.getId(); 
    Long moduleComponentId = moduleDAO.createModuleComponent(module, 16.75, moduleTimeUnit, "Test", "Desc").getId();
    
    commit();
    
    beginTransaction();
    
    ModuleComponent moduleComponent = moduleDAO.getModuleComponent(moduleComponentId);
    
    Assert.assertTrue(moduleComponent instanceof ModuleComponent);
    Assert.assertEquals(moduleComponent.getId(), moduleComponentId);
    
    moduleDAO.deleteModuleComponent(moduleComponent);
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(moduleTimeUnit.getId()));
    
    commit();
  }
  
  @Test
  public void testListModules() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    Long subjectId = subject.getId();
    EducationalTimeUnit educationalTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    
    TestDataGenerator.createTestModules(10, 0, "Module", "Desc", getTestUser(), subject, educationalTimeUnit);
  
    commit();
    
    beginTransaction();
    
    List<Module> modules = moduleDAO.listModules();
    
    Assert.assertEquals(modules.size(), 10);
    for (int i = 0; i < modules.size(); i++) {
      moduleDAO.deleteModule(moduleDAO.getModule(modules.get(i).getId()));
    }
    
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(educationalTimeUnit.getId()));
    
    commit();
  }

  @Test
  public void testUpdateModule() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    final Long subjectId = subject.getId();
    EducationalTimeUnit moduleTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    Module module = moduleDAO.createModule(MODULE_NAME, baseDAO.getSubject(subjectId), 1, MODULE_LENGTH, moduleTimeUnit, MODULE_DESCRIPTION, getTestUser());
    final Long moduleId = module.getId(); 
    
    commit();
    
    /* name null & empty tests */
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        Module module = moduleDAO.getModule(moduleId);
        moduleDAO.updateModule(module, null, baseDAO.getSubject(subjectId), 1, 12.34, module.getCourseLength().getUnit(), "desc", getTestUser());
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        Module module = moduleDAO.getModule(moduleId);
        moduleDAO.updateModule(module, "", baseDAO.getSubject(subjectId), 1, 12.34, module.getCourseLength().getUnit(), "desc", getTestUser());
      }
    });
    
    beginTransaction();
    
    /* and clean up */ 
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(moduleTimeUnit.getId()));
    
    commit();
  }

  @Test
  public void testUpdateModuleComponent() {
    beginTransaction();
    
    Subject subject = baseDAO.createSubject("123", "Test subject");
    Long subjectId = subject.getId();
    EducationalTimeUnit moduleTimeUnit = baseDAO.createEducationalTimeUnit(1.0, "nothing");
    
    Module module = moduleDAO.createModule(MODULE_NAME, baseDAO.getSubject(subjectId), 1, MODULE_LENGTH, moduleTimeUnit, MODULE_DESCRIPTION, getTestUser());
    Long moduleId = module.getId(); 
    ModuleComponent moduleComponent = moduleDAO.createModuleComponent(module, MODULE_COMPONENT_LENGTH, moduleTimeUnit, MODULE_COMPONENT_NAME, MODULE_COMPONENT_DESCRIPTION);
    final Long moduleComponentId = moduleComponent.getId();
    
    commit();
    
    /* name null & empty tests */
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        ModuleComponent moduleComponent = moduleDAO.getModuleComponent(moduleComponentId);
        moduleDAO.updateModuleComponent(moduleComponent, MODULE_COMPONENT_LENGTH, moduleComponent.getLength().getUnit(), null, MODULE_COMPONENT_DESCRIPTION);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        ModuleComponent moduleComponent = moduleDAO.getModuleComponent(moduleComponentId);
        moduleDAO.updateModuleComponent(moduleComponent, MODULE_COMPONENT_LENGTH, moduleComponent.getLength().getUnit(), "", MODULE_COMPONENT_DESCRIPTION);
      }
    });
    
    beginTransaction();
    
    /* and clean up */ 
    moduleDAO.deleteModuleComponent(moduleDAO.getModuleComponent(moduleComponentId));
    moduleDAO.deleteModule(moduleDAO.getModule(moduleId));
    baseDAO.deleteSubject(baseDAO.getSubject(subjectId));
    baseDAO.deleteEducationalTimeUnit(baseDAO.getEducationalTimeUnit(moduleTimeUnit.getId()));
    
    commit();
  }

  @Test
  public void testSearchModules() {
    // TODO: 
  }

  private ModuleDAO moduleDAO = new ModuleDAO();
  private UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
  private BaseDAO baseDAO = new BaseDAO();
  private Long userId;
}
