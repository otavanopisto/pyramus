package fi.pyramus.domainmodel.base;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.testutils.DatabaseDependingTest;

@Test
public class EducationTypeTest extends DatabaseDependingTest {

  @BeforeClass
  protected void setUp() throws Exception {
  }

  @AfterClass
  protected void tearDown() throws Exception {
  }

  @Test
  public void testGetId() {
    beginTransaction();
    EducationType educationType = new EducationType();
    educationType.setName("Education type");
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Long educationTypeId = educationType.getId(); 
    Assert.assertNotNull(educationTypeId);
    
    // Database cleanup
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(EducationType.class, educationTypeId));
    commit();
  }

  @Test
  public void testGetName() {
    beginTransaction();
    EducationType educationType = new EducationType();
    educationType.setName("Правда i.e. Pravda");
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Long educationTypeId = educationType.getId(); 
    beginTransaction();
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    Assert.assertEquals(educationType.getName(), "Правда i.e. Pravda");
    getCurrentSession().delete(educationType);
    commit();
  }

  @Test
  public void testSetName() {
    // TODO Tested in testGetName
  }

  @Test
  public void testGetSubtypes() {
    beginTransaction();
    EducationType educationType = new EducationType();
    educationType.setName("Education type");
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Long educationTypeId = educationType.getId(); 

    beginTransaction();
    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Subtype #1");
    educationType.addSubtype(educationSubtype);
    educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Subtype #2");
    educationType.addSubtype(educationSubtype);
    getCurrentSession().saveOrUpdate(educationType);
    commit();

    beginTransaction();
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    Assert.assertEquals(educationType.getSubtypes().size(), 2);
    educationType.removeSubtype(educationType.getSubtypes().get(0));
    getCurrentSession().saveOrUpdate(educationType);
    commit();

    beginTransaction();
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    Assert.assertEquals(educationType.getSubtypes().size(), 1);
    getCurrentSession().delete(educationType);
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

}
