package fi.pyramus.domainmodel.base;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.base.EducationSubtype;
import fi.pyramus.domainmodel.base.EducationType;
import fi.testutils.DatabaseDependingTest;


@Test
public class EducationSubtypeTest extends DatabaseDependingTest {

  @BeforeClass
  protected void setUp() throws Exception {
    beginTransaction();
    EducationType educationType = new EducationType();
    educationType.setName("Education type");
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    educationTypeId = educationType.getId();
  }

  @AfterClass
  protected void tearDown() throws Exception {
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(EducationType.class, educationTypeId));
    commit();
  }

  @Test
  public void testGetId() {
    beginTransaction();
    EducationType educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Education subtype");
    educationType.addSubtype(educationSubtype);
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Assert.assertNotNull(educationSubtype.getId());
  }

  @Test
  public void testGetName() {
    beginTransaction();
    EducationType educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Правда i.e. Pravda");
    educationType.addSubtype(educationSubtype);
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Long educationSubtypeId = educationSubtype.getId(); 
    beginTransaction();
    educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
    Assert.assertEquals(educationSubtype.getName(), "Правда i.e. Pravda");
    commit();
  }

  @Test
  public void testSetName() {
    // TODO Tested in testGetName
  }

  @Test
  public void testGetEducationType() {
    beginTransaction();
    EducationType educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    EducationSubtype educationSubtype = new EducationSubtype(educationType);
    educationSubtype.setName("Education subtype");
    educationType.addSubtype(educationSubtype);
    getCurrentSession().saveOrUpdate(educationType);
    commit();
    Long educationSubtypeId = educationSubtype.getId(); 
    beginTransaction();
    educationType = (EducationType) getCurrentSession().load(EducationType.class, educationTypeId);
    educationSubtype = (EducationSubtype) getCurrentSession().load(EducationSubtype.class, educationSubtypeId);
    Assert.assertEquals(educationSubtype.getEducationType(), educationType);
    commit();
  }
  
  private Long educationTypeId;

}
