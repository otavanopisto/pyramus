package fi.pyramus.domainmodel.resources;

import org.testng.Assert;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.resources.WorkResource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.testutils.DatabaseDependingTest;


@Test
public class WorkResourceTest extends DatabaseDependingTest {

  @Test
  public void testGetHourlyCost() {
    /* Normal setter/getter test */ 

    beginTransaction();
    WorkResource workResource = new WorkResource();
    workResource.setName("Test");
    //workResource.setCostPerUse(new MonetaryAmount(18.0));
    workResource.setHourlyCost(new MonetaryAmount(17.0));
    getCurrentSession().save(workResource);
    commit();
    
    beginTransaction();
    workResource = (WorkResource) getCurrentSession().load(WorkResource.class, workResource.getId());
    Assert.assertEquals("Test", workResource.getName());
    Assert.assertNotNull(workResource.getHourlyCost());
    Assert.assertEquals(workResource.getHourlyCost().getAmount(), 17.0);
    getCurrentSession().delete(workResource);
    commit();
  }

  @Test
  public void testGetCostPerUse() {
    /* Normal setter/getter test */ 

    beginTransaction();
    WorkResource workResource = new WorkResource();
    workResource.setName("Test");
    workResource.setCostPerUse(new MonetaryAmount(18.0));
    getCurrentSession().save(workResource);
    commit();
    
    beginTransaction();
    workResource = (WorkResource) getCurrentSession().load(WorkResource.class, workResource.getId());
    Assert.assertEquals("Test", workResource.getName());
    Assert.assertNotNull(workResource.getCostPerUse());
    Assert.assertEquals(workResource.getCostPerUse().getAmount(), 18.0);
    getCurrentSession().delete(workResource);
    commit();
  }

}
