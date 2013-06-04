package fi.pyramus.domainmodel.resources;

import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.testutils.DatabaseDependingTest;


@Test
public class ResourceTest extends DatabaseDependingTest {

  @BeforeMethod
  protected void setUp() throws Exception {
    beginTransaction();
    
    ResourceCategory category = new ResourceCategory();
    category.setName("Test category");
    getCurrentSession().save(category);
    
    Resource resource = new WorkResource();
    resource.setCategory(category);
    resource.setName("Test resource");
    getCurrentSession().save(resource);
    
    commit();
    
    resourceId = resource.getId();
    resourceCategoryId = category.getId();
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    beginTransaction();
    
    getCurrentSession().delete(getCurrentSession().load(Resource.class, resourceId));
    getCurrentSession().delete(getCurrentSession().load(ResourceCategory.class, resourceCategoryId));
    
    commit();
  }

  @Test
  public void testGetId() {
    beginTransaction();
    Resource resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    Assert.assertNotNull(resource.getId());
    Assert.assertEquals(resource.getId(), resourceId);
    commit();
  }
  
  @Test
  public void testGetName() {
    /* Normal getter test */
    
    beginTransaction();
    Resource resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    resource.setName("Changed");
    getCurrentSession().update(resource);
    commit();
    
    beginTransaction();
    resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    Assert.assertEquals(resource.getName(), "Changed");
    commit();
  }

  @Test
  public void testSetName() {
    /* Normal getter test is already performed in testGetName -method, so we only need to do null and empty tests */
    
    beginTransaction();
    try {
      Resource resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
      resource.setName(null);
      getCurrentSession().update(resource);
      commit();
      Assert.fail("Resource.setName(null); succeeded");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
    beginTransaction();
    try {
      Resource resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
      resource.setName("");
      getCurrentSession().update(resource);
      commit();
      
      Assert.fail("Resource.setName with empty string succeeded");
    } catch (InvalidStateException ise) {
      rollback();
    }
  }

  @Test
  public void testGetCategory() {
    beginTransaction();
    Resource resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    ResourceCategory resourceCategory = new ResourceCategory();
    resourceCategory.setName("Test");
    getCurrentSession().save(resourceCategory);
    
    resource.setCategory(resourceCategory);
    getCurrentSession().update(resource);
    
    commit();
    
    Long categoryId = resourceCategory.getId();
    
    beginTransaction();
    resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    resourceCategory = (ResourceCategory) getCurrentSession().load(ResourceCategory.class, categoryId);
    Assert.assertEquals(resource.getCategory().getId(), categoryId);
    commit();
    
    beginTransaction();
    resource = (Resource) getCurrentSession().load(Resource.class, resourceId);
    resource.setCategory(null);
    getCurrentSession().delete(getCurrentSession().load(ResourceCategory.class, categoryId));
    commit();
  }

  private Long resourceId;
  private Long resourceCategoryId;
}
