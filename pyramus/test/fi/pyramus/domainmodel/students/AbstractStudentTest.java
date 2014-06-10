package fi.pyramus.domainmodel.students;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fi.pyramus.persistence.usertypes.Sex;
import fi.testutils.DatabaseDependingTest;

@SuppressWarnings("deprecation")
public class AbstractStudentTest extends DatabaseDependingTest {

  private static Date TEST_BIRTHDAY = new Date(1990, 6, 24); 
  
  @BeforeMethod
  protected void setUp() throws Exception {
  }

  @AfterMethod
  protected void tearDown() throws Exception {
  }

  @Test
  public void testSuccessCreate() {
    beginTransaction();
    AbstractStudent abstractStudent = new AbstractStudent();
    abstractStudent.setBirthday(TEST_BIRTHDAY);
    abstractStudent.setSocialSecurityNumber("123D");
    abstractStudent.setSex(Sex.FEMALE);
    getCurrentSession().save(abstractStudent);
    commit();

    Long abstractStudentId = abstractStudent.getId();
        
    beginTransaction();
    abstractStudent = (AbstractStudent) getCurrentSession().load(AbstractStudent.class, abstractStudentId);

    Assert.assertNotNull(abstractStudent);
    Assert.assertEquals(abstractStudent.getBirthday().getTime(), TEST_BIRTHDAY.getTime());
    Assert.assertEquals(abstractStudent.getSocialSecurityNumber(), "123D");
    Assert.assertEquals(abstractStudent.getSex(), Sex.FEMALE);
    
    getCurrentSession().delete(abstractStudent);
    commit();
  }

  @Test
  public void testFailingCreate() {
    try {
      AbstractStudent abstractStudent;

      try {
        beginTransaction();
        abstractStudent = new AbstractStudent();
        abstractStudent.setBirthday(TEST_BIRTHDAY);
        abstractStudent.setSocialSecurityNumber("123D");
        // Sex is not defined
        //abstractStudent.setSex(Sex.FEMALE);
        getCurrentSession().save(abstractStudent);
        commit();
      } catch (Exception e) {
        rollback();
        throw e;
      }
      
      beginTransaction();
      getCurrentSession().delete(abstractStudent);
      commit();
      
      Assert.fail("Creating AbstractStudent succeeded with undefined sex.");
    } catch (Exception e) {      
    }
  }

}
