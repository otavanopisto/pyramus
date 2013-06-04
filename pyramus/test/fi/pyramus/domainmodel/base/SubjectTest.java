
package fi.pyramus.domainmodel.base;
import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.Test;



import fi.pyramus.domainmodel.base.Subject;
import fi.testutils.DatabaseDependingTest;

public class SubjectTest extends DatabaseDependingTest {

  private static String TESTNAME = "Правда - Pravda";
  private static String TESTCODE = "1001";
  
  @Test
  public void testGetId() {
    /* Simple object creation test */ 
    
    beginTransaction();
    Subject subject = new Subject();
    subject.setCode(TESTCODE);
    subject.setName(TESTNAME);
    getCurrentSession().save(subject);
    commit();
    
    Long id = subject.getId();
    
    beginTransaction();
    subject = (Subject) getCurrentSession().load(Subject.class, id);
    Assert.assertNotNull(subject);
    commit();
   
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(Subject.class, id));
    commit();
  }

  @Test
  public void testSetName() {
/* Normal setter/getter test is already included in testGetName test so we just perform the null test */
    
    beginTransaction();
    try {
      Subject subject = new Subject();
      subject.setCode(TESTCODE);
      subject.setName(null);
      getCurrentSession().save(subject);
      
      commit();
      
      Assert.fail("Subject.setName(null); succeeded");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
    /* ... and the empty tests */ 
    
    beginTransaction();
    try {
      Subject subject = new Subject();
      subject.setCode(TESTCODE);
      subject.setName("");
      getCurrentSession().save(subject);
      
      commit();
      
      Assert.fail("Subject.setName with empty string succeeded");
    } catch (InvalidStateException ise) {
      rollback();
    }
  }

  @Test
  public void testGetName() {
    /* Normal setter/getter test */ 
    
    beginTransaction();
    Subject subject = new Subject();
    subject.setCode(TESTCODE);
    subject.setName(TESTNAME);
    getCurrentSession().save(subject);
    commit();
    
    beginTransaction();
    subject = (Subject) getCurrentSession().load(Subject.class, subject.getId());
    Assert.assertEquals(TESTNAME, subject.getName());
    commit();
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(Subject.class, subject.getId()));
    commit();
  }

  @Test
  public void testGetCode() {
    /* Normal setter/getter test */ 
    
    beginTransaction();
    Subject subject = new Subject();
    subject.setCode(TESTCODE);
    subject.setName(TESTNAME);
    getCurrentSession().save(subject);
    commit();
    
    beginTransaction();
    subject = (Subject) getCurrentSession().load(Subject.class, subject.getId());
    Assert.assertEquals(TESTCODE, subject.getCode());
    commit();
  
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(Subject.class, subject.getId()));
    commit();
  }

  @Test
  public void testSetCode() {
    beginTransaction();
    try {
      Subject subject = new Subject();
      subject.setCode(null);
      subject.setName(TESTNAME);
      getCurrentSession().save(subject);
      
      commit();
      
      Assert.fail("Subject.setCode(null); succeeded");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
  }
}
