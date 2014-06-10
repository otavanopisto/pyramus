package fi.pyramus.dao;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;



@Test 
public class BaseDAOTest extends DatabaseDependingTest {

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
  
  @Test
  public void testCreateSubject() {
    String name = "Lorem ipsum";
    String code = "1001";
    
    beginTransaction();
    Subject subject = baseDAO.createSubject(code, name);
    commit();
    
    Assert.assertNotNull(subject.getId());
    
    beginTransaction();
    subject = baseDAO.getSubject(subject.getId());
    Assert.assertEquals(subject.getName(), name);
    Assert.assertEquals(subject.getCode(), code);
    baseDAO.deleteSubject(subject);
    commit();
  }

  @Test
  public void testGetSubject() {
    String name = "Lorem ipsum";
    String code = "1001";
    
    beginTransaction();

    Subject subject = baseDAO.createSubject(code, name);
    
    commit();
    
    beginTransaction();
    
    Subject gotSubject = baseDAO.getSubject(subject.getId());

    Assert.assertNotNull(gotSubject.getId());
    Assert.assertEquals(gotSubject.getName(), name);
    Assert.assertEquals(gotSubject.getCode(), code);
    
    commit();
    
    beginTransaction();
    baseDAO.deleteSubject(baseDAO.getSubject(subject.getId()));
    commit();
  }

  @Test
  public void testListSubjects() {
    beginTransaction();

    Subject subject1 = baseDAO.createSubject("1001", "Aaaaaa");
    Subject subject2 = baseDAO.createSubject("1002", "Beeeee");
    Subject subject3 = baseDAO.createSubject("1003", "Ceeeee");
    
    Assert.assertEquals(baseDAO.listSubjects().size(), 3);
    
    commit();
    
    beginTransaction();
    baseDAO.deleteSubject(baseDAO.getSubject(subject1.getId()));
    baseDAO.deleteSubject(baseDAO.getSubject(subject2.getId()));
    baseDAO.deleteSubject(baseDAO.getSubject(subject3.getId()));
    commit();
  }

  @Test
  public void testDeleteEducationSubtype() {
    beginTransaction();
    
    EducationType educationType = baseDAO.createEducationType("type", "code");
    EducationSubtype educationSubtype = baseDAO.createEducationSubtype(educationType, "subtype", "code");
    
    Long educationTypeId = educationType.getId();
    Long educationSubtypeId = educationSubtype.getId();
    
    commit();
    
    beginTransaction();
    
    baseDAO.deleteEducationSubtype(baseDAO.getEducationSubtype(educationSubtypeId));
    baseDAO.deleteEducationType(baseDAO.getEducationType(educationTypeId));
    
    commit();
  }

  @Test
  public void testGetEducationSubtype() {
    beginTransaction();
    
    EducationType educationType = baseDAO.createEducationType("type", "code");
    EducationSubtype educationSubtype = baseDAO.createEducationSubtype(educationType, "subtype", "code");
    
    Long educationTypeId = educationType.getId();
    Long educationSubtypeId = educationSubtype.getId();
    
    commit();
    
    beginTransaction();
    
    educationSubtype = baseDAO.getEducationSubtype(educationSubtypeId);
    
    Assert.assertTrue(educationSubtype instanceof EducationSubtype);
    Assert.assertEquals(educationSubtype.getId(), educationSubtypeId);
    
    baseDAO.deleteEducationSubtype(baseDAO.getEducationSubtype(educationSubtypeId));
    baseDAO.deleteEducationType(baseDAO.getEducationType(educationTypeId));
    
    commit();
  }

  @Test
  public void testListEducationTypes() {
    beginTransaction();
    
    TestDataGenerator.createTestEducationTypes(10, 0, "test");
    
    commit();
    
    beginTransaction();

    List<EducationType> educationTypes = baseDAO.listEducationTypes();
    Assert.assertEquals(educationTypes.size(), 10);
    
    for (int i = 0; i < educationTypes.size(); i++) {
      baseDAO.deleteEducationType(baseDAO.getEducationType(educationTypes.get(i).getId()));
    }
    
    commit();
  }
  
  private BaseDAO baseDAO = new BaseDAO();
  private UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
  private Long userId;
}
