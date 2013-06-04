package fi.pyramus.domainmodel.resources;

import org.hibernate.PropertyValueException;
import org.testng.Assert;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.resources.MaterialResource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.testutils.DatabaseDependingTest;


@Test
public class MaterialResourceTest extends DatabaseDependingTest {

  // TODO: Unicode tests
  
  @Test
  public void testGetUnitCost() {
    /* Normal setter/getter test */ 

    beginTransaction();
    MaterialResource materialResource = new MaterialResource();
    materialResource.setName("Test");
    materialResource.setUnitCost(new MonetaryAmount(15.0));
    getCurrentSession().save(materialResource);
    commit();
    
    beginTransaction();
    materialResource = (MaterialResource) getCurrentSession().load(MaterialResource.class, materialResource.getId());
    Assert.assertEquals("Test", materialResource.getName());
    Assert.assertEquals(materialResource.getUnitCost().getAmount(), 15.0);
    getCurrentSession().delete(materialResource);
    commit();
  }

  @Test
  public void testSetUnitCost() {
    /* Normal setter/getter test is already included in testGetUnitCost test so we just perform the null test */
    
    beginTransaction();
    try {
      MaterialResource materialResource = new MaterialResource();
      materialResource.setName("Test");
      materialResource.setUnitCost(null);
      getCurrentSession().save(materialResource);
      commit();
      
      Assert.fail("ResourceCategory.setUnitCost(null); succeeded");
    } catch (PropertyValueException pve) {
      rollback();
    }
  }

}
