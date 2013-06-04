package fi.pyramus.domainmodel.resources;

import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.testutils.DatabaseDependingTest;


@Test
public class ResourceCategoryTest extends DatabaseDependingTest {

  @Test
  public void testGetId() {
    /* Simple creation test */ 
    
    beginTransaction();
    ResourceCategory category = new ResourceCategory();
    category.setName("Test");
    getCurrentSession().save(category);
    commit();
    
    beginTransaction();
    category = (ResourceCategory) getCurrentSession().load(ResourceCategory.class, category.getId());
    Assert.assertNotNull(category.getId());
    commit();
    
    /* Clean up */ 
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(ResourceCategory.class, category.getId()));
    commit();
    
  }

  @Test
  public void testGetName() {
    /* Normal setter/getter test */ 
    
    beginTransaction();
    ResourceCategory category = new ResourceCategory();
    category.setName("Test");
    getCurrentSession().save(category);
    commit();
    
    beginTransaction();
    category = (ResourceCategory) getCurrentSession().load(ResourceCategory.class, category.getId());
    Assert.assertEquals("Test", category.getName());
    commit();
    
    /* Clean up */ 
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(ResourceCategory.class, category.getId()));
    commit();
  }

  @Test
  public void testSetName() {
    /* Normal setter/getter test is already included in testGetName test so we just perform the null test */
    
    beginTransaction();
    try {
      ResourceCategory category = new ResourceCategory();
      category.setName(null);
      getCurrentSession().save(category);
      commit();
      
      Assert.fail("ResourceCategory.setName(null); succeeded");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
    /* ... and the empty tests */ 
    
    beginTransaction();
    try {
      ResourceCategory category = new ResourceCategory();
      category.setName("");
      getCurrentSession().save(category);
      commit();
      
      Assert.fail("ResourceCategory.setName with empty string succeeded");
    } catch (InvalidStateException ise) {
      rollback();
    }
  }
}
