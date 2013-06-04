package fi.pyramus.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fi.pyramus.domainmodel.resources.MaterialResource;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.domainmodel.resources.WorkResource;
import fi.testutils.DatabaseDependingTest;


public class ResourceDAOTest extends DatabaseDependingTest {

  private static String MATERIAL_NAME = "Правда - Truth";
  private static String WORKRESOURCE_NAME = "铲 -  Shovel";
  private static String CATEGORY_NAME = "やかん - Teakettle";
  
  @BeforeClass
  protected void setUp() throws Exception {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory(CATEGORY_NAME);
    commit();
    
    resourceCategoryId = resourceCategory.getId();
  }

  @AfterClass
  protected void tearDown() throws Exception {
    beginTransaction();
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategoryId));
    commit();
  }
  
  @Test
  public void testCreateMaterialResource() {
    long resourceId;
    /* Normal resource creation test */ 
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    MaterialResource materialResource = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceCategory, 12.0);
    commit();
    
    resourceId = materialResource.getId();
    
    /* Normal checks if its still ok */
  
    beginTransaction();
    materialResource = (MaterialResource) resourceDAO.getMaterialResource(resourceId);
    Assert.assertEquals(materialResource.getUnitCost().getAmount(), 12.0);
    Assert.assertEquals(materialResource.getName(), MATERIAL_NAME);
    commit();
    
    /* Clean up */
    
    beginTransaction();
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    commit();
    
    /* Null name check */
    
    beginTransaction();
    try {
      materialResource = resourceDAO.createMaterialResource(null, resourceCategory, 12.0);
      commit();
      Assert.fail("Created resource with null name");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
    /* Empty name check */
    
    beginTransaction();
    try {
      materialResource = resourceDAO.createMaterialResource("", resourceCategory, 12.0);
      commit();
      Assert.fail("Created resource with empty name");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
    /* Null cost check */
    
    beginTransaction();
    try {
      materialResource = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceCategory, null);
      commit();
      Assert.fail("Created resource with null cost");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
// TODO Needs to be tested in MonetaryAmount
//    /* Negative cost check */
//    
//    beginTransaction();
//    try {
//      materialResource = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceCategory, -1.0);
//      commit();
//      Assert.fail("Created resource with negative cost");
//    } catch (InvalidStateException ise) {
//      rollback();
//    }
  }
  
  @Test
  public void testCreateWorkResource() {
    long resourceId;
    
    /* Normal resource creation test */ 
    
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    WorkResource workResource = resourceDAO.createWorkResource(WORKRESOURCE_NAME, resourceCategory, 24.0, 36.0);
    commit();
    
    resourceId = workResource.getId();
    
    /* Normal checks if its still ok */
  
    beginTransaction();
    workResource = (WorkResource) resourceDAO.getWorkResource(resourceId);
    Assert.assertEquals(workResource.getCostPerUse().getAmount(), 24.0);
    Assert.assertEquals(workResource.getHourlyCost().getAmount(), 36.0);
    Assert.assertEquals(workResource.getName(), WORKRESOURCE_NAME);
    commit();
    
    /* Clean up */
    
    beginTransaction();
    resourceDAO.deleteResource(resourceDAO.getResource(resourceId));
    commit();
    
    /* Null name check */
    
    beginTransaction();
    try {
      workResource = resourceDAO.createWorkResource(null, resourceCategory, 24.0, 36.0);
      commit();
      Assert.fail("Created resource with null name");
    } catch (PropertyValueException pve) {
      rollback();
    }
    
    /* Empty name check */
    
    beginTransaction();
    try {
      workResource = resourceDAO.createWorkResource("", resourceCategory, 24.0, 36.0);
      commit();
      Assert.fail("Created resource with empty name");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
 // TODO These should fail in MonetaryAmount
//    /* Negative cost check */
//    
//    beginTransaction();
//    try {
//      workResource = resourceDAO.createWorkResource(MATERIAL_NAME, resourceCategory, -1.0, 36.0);
//      commit();
//      Assert.fail("Created workresource with negative costPerUse");
//    } catch (InvalidStateException ise) {
//      rollback();
//    }
//    
//    /* Negative cost check */
//    
//    beginTransaction();
//    try {
//      workResource = resourceDAO.createWorkResource(MATERIAL_NAME, resourceCategory, 24.0, -1.0);
//      commit();
//      Assert.fail("Created workresource with negative hourlyCost");
//    } catch (InvalidStateException ise) {
//      rollback();
//    }
    
    /* Both costs should be nullable */
    
    beginTransaction();
    workResource = resourceDAO.createWorkResource(MATERIAL_NAME, resourceCategory, null, null);
    commit();
    
    /* Clean up */
    
    beginTransaction();
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    commit();
  }
  
  @Test
  public void testCreateResourceCategory() {
    /* Category creation check */ 
    
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory(CATEGORY_NAME);
    commit();
    
    beginTransaction();
    resourceCategory = resourceDAO.getResourceCategory(resourceCategory.getId());
    Assert.assertEquals(resourceCategory.getName(), CATEGORY_NAME);
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit();
    
    /* Null name check */
    
    beginTransaction();
    try {
      resourceCategory = resourceDAO.createResourceCategory(null);
      commit();
      Assert.fail("Created resource category with null name");
    } catch (PropertyValueException ise) {
      rollback();
    }
    
    /* Empty name check */
    
    beginTransaction();
    try {
      resourceCategory = resourceDAO.createResourceCategory("");
      commit();
      Assert.fail("Created resource category with null name");
    } catch (InvalidStateException ise) {
      rollback();
    }
  }
  
  @Test
  public void testListResourceCategories() {
    beginTransaction();
    List<ResourceCategory> categoryList = resourceDAO.listResourceCategories();
    Assert.assertEquals(categoryList.size(), 1);
    commit();
  }
  
  @Test
  public void testListResources() {
    /* Few test resources, 1 material and 1 work resource */ 
    
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    MaterialResource materialResource = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceCategory, 12.0);
    WorkResource workResource = resourceDAO.createWorkResource(WORKRESOURCE_NAME, resourceCategory, 24.0, 36.0);
    commit();
    
    /* Check if we have 2 resources, 1 material and 1 work resource */ 
    
    beginTransaction();
    List<Resource> resources = resourceDAO.listResources();
    Assert.assertEquals(resources.size(), 2);
    
    List<WorkResource> workResources = resourceDAO.listWorkResources();
    Assert.assertEquals(workResources.size(), 1);
    
    List<MaterialResource> materialResources = resourceDAO.listMaterialResources();
    Assert.assertEquals(materialResources.size(), 1);
    commit();
    
    /* clean up */
    
    beginTransaction();
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    commit();
  }

  @Test
  public void testGetResourceCategory() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    Assert.assertNotNull(resourceCategory.getId());
    Assert.assertNotNull(resourceCategory.getName());
    Assert.assertFalse(resourceCategory.getName().isEmpty());
    commit();
  }
  
  @Test
  public void testGetMaterialResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    MaterialResource resource = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceCategory, 1.0);
    commit();
    
    beginTransaction();
    resource = resourceDAO.getMaterialResource(resource.getId());
    Assert.assertNotNull(resource.getId());
    Assert.assertNotNull(resource.getName());
    Assert.assertFalse(resource.getName().isEmpty());
    Assert.assertEquals(resource.getUnitCost().getAmount(), 1.0);
    
    resourceDAO.deleteResource(resourceDAO.getResource(resource.getId()));
    commit();
  }
  
  @Test
  public void testGetWorkResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    WorkResource resource = resourceDAO.createWorkResource(MATERIAL_NAME, resourceCategory, 1.0, 2.0);
    commit();
    
    beginTransaction();
    resource = resourceDAO.getWorkResource(resource.getId());
    Assert.assertNotNull(resource.getId());
    Assert.assertNotNull(resource.getName());
    Assert.assertFalse(resource.getName().isEmpty());
    Assert.assertEquals(resource.getCostPerUse().getAmount(), 1.0);
    Assert.assertEquals(resource.getHourlyCost().getAmount(), 2.0);
    
    resourceDAO.deleteResource(resourceDAO.getResource(resource.getId()));
    commit();
  }
  
  @Test
  public void testDeleteResource() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.getResourceCategory(resourceCategoryId);
    MaterialResource materialResource = resourceDAO.createMaterialResource(WORKRESOURCE_NAME, resourceCategory, 1.0);
    WorkResource workResource = resourceDAO.createWorkResource(MATERIAL_NAME, resourceCategory, 1.0, 2.0);
    commit();
    
    beginTransaction();
    resourceDAO.deleteResource(resourceDAO.getResource(materialResource.getId()));
    resourceDAO.deleteResource(resourceDAO.getResource(workResource.getId()));
    commit();
  }
  
  @Test
  public void testDeleteResourceCategory() {
    beginTransaction();
    ResourceCategory resourceCategory = resourceDAO.createResourceCategory("Will be removed");
    commit();
    
    beginTransaction();
    resourceDAO.deleteResourceCategory(resourceDAO.getResourceCategory(resourceCategory.getId()));
    commit();
  }
  

  @Test
  public void testGetResource() {
    beginTransaction();
    Long wResourceId = resourceDAO.createWorkResource("Work", resourceDAO.getResourceCategory(resourceCategoryId), 5.5, 6.6).getId();
    Long mResourceId = resourceDAO.createMaterialResource("Material", resourceDAO.getResourceCategory(resourceCategoryId), 4.6).getId();
    commit();
    
    
    beginTransaction();
    Resource wResource = resourceDAO.getResource(wResourceId);
    Assert.assertEquals(wResource.getName(), "Work");
    Assert.assertEquals(wResource.getCategory().getId(), resourceCategoryId);
    
    Resource mResource = resourceDAO.getResource(mResourceId);
    Assert.assertEquals(mResource.getName(), "Material");
    Assert.assertEquals(mResource.getCategory().getId(), resourceCategoryId);
    
    resourceDAO.deleteResource(resourceDAO.getResource(wResourceId));
    resourceDAO.deleteResource(resourceDAO.getResource(mResourceId));
    
    commit();
  }

  @Test
  public void testUpdateMaterialResource() {
    beginTransaction();
    
    final Long resourceId = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceDAO.getResourceCategory(resourceCategoryId), 15.15).getId();
    
    commit();
    
    /* Null & empty name checks */
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        resourceDAO.updateMaterialResource(resourceDAO.getMaterialResource(resourceId), null, resourceDAO.getResourceCategory(resourceCategoryId), 12.0);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        resourceDAO.updateMaterialResource(resourceDAO.getMaterialResource(resourceId), "", resourceDAO.getResourceCategory(resourceCategoryId), 12.0);
      }
    });
    
    beginTransaction();
    
    resourceDAO.deleteResource(resourceDAO.getMaterialResource(resourceId));
  
    commit();
  }

  @Test
  public void testUpdateWorkResource() {
    beginTransaction();
    
    final Long resourceId = resourceDAO.createWorkResource(WORKRESOURCE_NAME, resourceDAO.getResourceCategory(resourceCategoryId), 15.15, 16.16).getId();
    
    commit();
    
    /* Null & empty name checks */
    checkProperty("name", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        resourceDAO.updateWorkResource(resourceDAO.getWorkResource(resourceId), null, resourceDAO.getResourceCategory(resourceCategoryId), 12.0, 13.0);
      }
    });
    checkProperty("name", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        resourceDAO.updateWorkResource(resourceDAO.getWorkResource(resourceId), "", resourceDAO.getResourceCategory(resourceCategoryId), 12.0, 13.0);
      }
    });
    
    beginTransaction();
    
    resourceDAO.deleteResource(resourceDAO.getWorkResource(resourceId));
  
    commit();
  }
  
  @Test
  public void testArchiveResource() {
    beginTransaction();
    
    final Long wResourceId = resourceDAO.createWorkResource(WORKRESOURCE_NAME, resourceDAO.getResourceCategory(resourceCategoryId), 15.15, 16.16).getId();
    final Long mResourceId = resourceDAO.createMaterialResource(MATERIAL_NAME, resourceDAO.getResourceCategory(resourceCategoryId), 15.15).getId();
    
    commit();
    
    beginTransaction();
    
    Assert.assertEquals(resourceDAO.getWorkResource(wResourceId).getArchived(), Boolean.FALSE);
    Assert.assertEquals(resourceDAO.getMaterialResource(mResourceId).getArchived(), Boolean.FALSE);
    resourceDAO.archiveResource(resourceDAO.getWorkResource(wResourceId));
    resourceDAO.archiveResource(resourceDAO.getMaterialResource(mResourceId));
    
    commit();
    
    beginTransaction();
    
    Assert.assertEquals(resourceDAO.getWorkResource(wResourceId).getArchived(), Boolean.TRUE);
    Assert.assertEquals(resourceDAO.getMaterialResource(mResourceId).getArchived(), Boolean.TRUE);
    
    commit();
    
    beginTransaction();
    
    resourceDAO.deleteResource(resourceDAO.getWorkResource(wResourceId));
    resourceDAO.deleteResource(resourceDAO.getMaterialResource(mResourceId));
    
    commit();
  }
  
  private Long resourceCategoryId;
  private ResourceDAO resourceDAO = new ResourceDAO();
  
}
