package fi.pyramus.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.pyramus.rest.CalendarRESTService;
import fi.pyramus.rest.CommonRESTService;
import fi.pyramus.rest.CourseRESTService;
import fi.pyramus.rest.ModuleRESTService;
import fi.pyramus.rest.ProjectRESTService;
import fi.pyramus.rest.ReportRESTService;
import fi.pyramus.rest.SchoolRESTService;
import fi.pyramus.rest.TagRESTService;
import fi.pyramus.rest.controller.CalendarController;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.controller.ReportController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.AcademicTermEntity;
import fi.pyramus.rest.tranquil.base.EducationTypeEntity;
import fi.pyramus.rest.tranquil.base.EducationalTimeUnitEntity;
import fi.pyramus.rest.tranquil.base.SchoolEntity;
import fi.pyramus.rest.tranquil.base.SchoolFieldEntity;
import fi.pyramus.rest.tranquil.base.SchoolVariableEntity;
import fi.pyramus.rest.tranquil.base.SubjectEntity;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.grading.GradingScaleEntity;
import fi.pyramus.rest.tranquil.modules.ModuleEntity;
import fi.pyramus.rest.tranquil.projects.ProjectEntity;
import fi.pyramus.rest.tranquil.projects.ProjectModuleEntity;
import fi.pyramus.rest.tranquil.reports.ReportCategoryEntity;
import fi.pyramus.rest.tranquil.reports.ReportEntity;

@RunWith(Arquillian.class)
public class AllRESTServiceTests extends RestfulServiceTest {

  @Inject
  private SchoolController schoolController;

  @Inject
  private InitialSchoolDataDescriptor initialSchoolDataDescriptor;

  public AllRESTServiceTests() throws URISyntaxException {
    super();
  }

  @Deployment
  @OverProtocol("Servlet 3.0")
  public static Archive<?> createTestArchive() {
    Archive<?> archive = createArchive(InitialSchoolDataDescriptor.class, SchoolController.class, SchoolRESTService.class, ProjectController.class,
        ProjectRESTService.class, ReportController.class, ReportRESTService.class, TagController.class, TagRESTService.class, CommonController.class,
        CommonRESTService.class, ModuleController.class, ModuleRESTService.class, CalendarController.class, CalendarRESTService.class, CourseController.class,
        CourseRESTService.class);

    return archive;
  }

  @Test
  public void testSanity() {
    assertNotNull(schoolController);
    School school = schoolController.findSchoolById(initialSchoolDataDescriptor.getSchoolId());
    assertNotNull(school);
    assertEquals(initialSchoolDataDescriptor.getSchoolId(), school.getId());
  }

  // SchoolRESTServiceTests

  @Test
  public void testCreateSchool() throws ClientProtocolException, IOException {
    SchoolEntity schoolEntity = new SchoolEntity();
    schoolEntity.setField_id(1l);
    schoolEntity.setCode("TAMK");
    schoolEntity.setName("Tampereen ammattikorkeakoulu");

    HttpResponse response = doPostRequest("/schools/schools/", schoolEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolEntity);
      assertEquals("Tampereen ammattikorkeakoulu", schoolEntity.getName());
      assertEquals("TAMK", schoolEntity.getCode());
      assertEquals((Long) 1l, schoolEntity.getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateSchoolField() throws ClientProtocolException, IOException {
    SchoolFieldEntity schoolFieldEntity = new SchoolFieldEntity();
    schoolFieldEntity.setName("Polytechnic");

    HttpResponse response = doPostRequest("/schools/schoolFields/", schoolFieldEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals("Polytechnic", schoolFieldEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateSchoolVariable() throws ClientProtocolException, IOException {
    SchoolVariableEntity schoolVariableEntity = new SchoolVariableEntity();
    schoolVariableEntity.setKey_id(1l);
    schoolVariableEntity.setSchool_id(1l);
    schoolVariableEntity.setValue("Test variable");
    
    HttpResponse response = doPostRequest("/schools/variables/", schoolVariableEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getSchool_id());
      assertEquals("Test variable", schoolVariableEntity.getValue());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchools() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity[] schoolEntities = unserializeEntity(SchoolEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolEntities);
      assertEquals(2, schoolEntities.length);
      assertEquals(initialSchoolDataDescriptor.getSchoolId(), schoolEntities[0].getId());
      assertEquals(initialSchoolDataDescriptor.getSchoolName(), schoolEntities[0].getName());
      assertEquals(initialSchoolDataDescriptor.getSchoolCode(), schoolEntities[0].getCode());
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolEntities[0].getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testSearchSchools() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools?code=TAMK");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity[] schoolEntities = unserializeEntity(SchoolEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolEntities);
      assertEquals(1, schoolEntities.length);
      assertEquals((Long) 2l, schoolEntities[0].getId());
      assertEquals("Tampereen ammattikorkeakoulu", schoolEntities[0].getName());
      assertEquals("TAMK", schoolEntities[0].getCode());
      assertEquals((Long) 1l, schoolEntities[0].getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools/" + initialSchoolDataDescriptor.getSchoolId());

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolEntity);
      assertEquals(initialSchoolDataDescriptor.getSchoolId(), schoolEntity.getId());
      assertEquals(initialSchoolDataDescriptor.getSchoolName(), schoolEntity.getName());
      assertEquals(initialSchoolDataDescriptor.getSchoolCode(), schoolEntity.getCode());
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolEntity.getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolFields() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schoolFields");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity[] schoolFieldEntities = unserializeEntity(SchoolFieldEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntities);
      assertEquals(2, schoolFieldEntities.length);
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolFieldEntities[0].getId());
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldName(), schoolFieldEntities[0].getName());
      assertEquals((Long) 2l, schoolFieldEntities[1].getId());
      assertEquals("Polytechnic", schoolFieldEntities[1].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolFieldByID() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schoolFields/" + initialSchoolDataDescriptor.getSchoolFieldId());

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolFieldEntity.getId());
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldName(), schoolFieldEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolVariables() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/variables");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity[] schoolVariableEntities = unserializeEntity(SchoolVariableEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntities);
      assertEquals(1, schoolVariableEntities.length);
      assertEquals((Long) 1l, schoolVariableEntities[0].getId());
      assertEquals("Test variable", schoolVariableEntities[0].getValue());
      assertEquals((Long) 1l, schoolVariableEntities[0].getSchool_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolVariableByID() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/variables/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getId());
      assertEquals("Test variable", schoolVariableEntity.getValue());
      assertEquals((Long) 1l, schoolVariableEntity.getSchool_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolVariablesBySchool() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools/1/variables");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity[] schoolVariableEntities = unserializeEntity(SchoolVariableEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntities);
      assertEquals(1, schoolVariableEntities.length);
      assertEquals((Long) 1l, schoolVariableEntities[0].getId());
      assertEquals("Test variable", schoolVariableEntities[0].getValue());
      assertEquals((Long) 1l, schoolVariableEntities[0].getSchool_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSchoolVariableBySchoolAndId() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools/1/variables/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getId());
      assertEquals("Test variable", schoolVariableEntity.getValue());
      assertEquals((Long) 1l, schoolVariableEntity.getSchool_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateSchool() throws ClientProtocolException, IOException {
    SchoolEntity schoolEntity = new SchoolEntity();
    schoolEntity.setField_id(2l);
    schoolEntity.setCode("SUST");
    schoolEntity.setName("Shahjalal University of Science and Technology");

    String path = "/schools/schools/1";

    HttpResponse response = doPutRequest(path, schoolEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolEntity);
      assertEquals(initialSchoolDataDescriptor.getSchoolId(), schoolEntity.getId());
      assertEquals("Shahjalal University of Science and Technology", schoolEntity.getName());
      assertEquals("SUST", schoolEntity.getCode());
      assertEquals((Long) 2l, schoolEntity.getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateSchoolField() throws ClientProtocolException, IOException {
    SchoolFieldEntity schoolFieldEntity = new SchoolFieldEntity();
    schoolFieldEntity.setName("College");
    
    String path = "/schools/schoolFields/1";
    
    HttpResponse response = doPutRequest(path, schoolFieldEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolFieldEntity.getId());
      assertEquals("College", schoolFieldEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateSchoolVariable() throws ClientProtocolException, IOException {
    SchoolVariableEntity schoolVariableEntity = new SchoolVariableEntity();
    schoolVariableEntity.setValue("foo bar value");
    
    String path = "/schools/variables/1";

    HttpResponse response = doPutRequest(path, schoolVariableEntity);

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getId());
      assertEquals("foo bar value", schoolVariableEntity.getValue());
      assertEquals((Long) 1l, schoolVariableEntity.getSchool_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveSchool() throws ClientProtocolException, IOException {
    String path = "/schools/schools/2";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolEntity);
      assertEquals((Long) 2l, schoolEntity.getId());
      assertEquals(true, schoolEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindUnarchivedSchools() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/schools/schools?filterArchived=true");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity[] schoolEntities = unserializeEntity(SchoolEntity[].class, EntityUtils.toString(entity));
      assertNotNull(schoolEntities);
      assertEquals(1, schoolEntities.length);
      assertEquals((Long) 1l, schoolEntities[0].getId());
      assertEquals("Shahjalal University of Science and Technology", schoolEntities[0].getName());
      assertEquals("SUST", schoolEntities[0].getCode());
      assertEquals((Long) 2l, schoolEntities[0].getField_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveSchool() throws ClientProtocolException, IOException {
    SchoolEntity schoolEntity = new SchoolEntity();
    schoolEntity.setArchived(false);
    
    String path = "/schools/schools/2";

    HttpResponse response = doPutRequest(path, schoolEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolEntity);
      assertEquals((Long) 2l, schoolEntity.getId());
      assertEquals(false, schoolEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveSchoolField() throws ClientProtocolException, IOException {
    String path = "/schools/schoolFields/2";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals((Long) 2l, schoolFieldEntity.getId());
      assertEquals(true, schoolFieldEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveSchoolField() throws ClientProtocolException, IOException {
    SchoolFieldEntity schoolFieldEntity = new SchoolFieldEntity();
    schoolFieldEntity.setArchived(false);
    
    String path = "/schools/schoolFields/2";

    HttpResponse response = doPutRequest(path, schoolFieldEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals((Long) 2l, schoolFieldEntity.getId());
      assertEquals(false, schoolFieldEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveSchoolVariable() throws ClientProtocolException, IOException {
    String path = "/schools/variables/1";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getId());
      assertEquals(true, schoolVariableEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveSchoolVariable() throws ClientProtocolException, IOException {
    SchoolVariableEntity schoolVariableEntity = new SchoolVariableEntity();
    schoolVariableEntity.setArchived(false);

    String path = "/schools/variables/1";

    HttpResponse response = doPutRequest(path, schoolVariableEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolVariableEntity);
      assertEquals((Long) 1l, schoolVariableEntity.getId());
      assertEquals(false, schoolVariableEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // ProjectRESTServiceTests

  @Test
  public void testCreateProject() throws ClientProtocolException, IOException {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setName("PyramusREST");
    projectEntity.setDescription("Pyramus RESTService development");

    HttpResponse response = doPostRequest("/projects/projects", projectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals("PyramusREST", projectEntity.getName());
      assertEquals("Pyramus RESTService development", projectEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateProjectTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("Test environment");
    
    HttpResponse response = doPostRequest("/projects/projects/1/tags", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals("Test environment", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindProjects() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity[] projectEntities = unserializeEntity(ProjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectEntities);
      assertEquals(1, projectEntities.length);
      assertEquals("PyramusREST", projectEntities[0].getName());
      assertEquals("Pyramus RESTService development", projectEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindProjectById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals("PyramusREST", projectEntity.getName());
      assertEquals("Pyramus RESTService development", projectEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testSearchProjects() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects?tags=Test%20environment");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity[] projectEntities = unserializeEntity(ProjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectEntities);
      assertEquals(1, projectEntities.length);
      assertEquals("PyramusREST", projectEntities[0].getName());
      assertEquals("Pyramus RESTService development", projectEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindProjectTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects/1/tags");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(1, tagEntities.length);
      assertEquals("Test environment", tagEntities[0].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateProject() throws ClientProtocolException, IOException {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setName("Updated project");
    projectEntity.setDescription("Four legged llama");
    
    String path = "/projects/projects/1";

    HttpResponse response = doPutRequest(path, projectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals("Updated project", projectEntity.getName());
      assertEquals("Four legged llama", projectEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveProject() throws ClientProtocolException, IOException {
    String path = "/projects/projects/1";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals(true, projectEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveProject() throws ClientProtocolException, IOException {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setArchived(false);
    
    String path = "/projects/projects/1";

    HttpResponse response = doPutRequest(path, projectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals(false, projectEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindUnarchivedProjects() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects?filterArchived=true");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity[] projectEntities = unserializeEntity(ProjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectEntities);
      assertEquals(1, projectEntities.length);
      assertEquals("Updated project", projectEntities[0].getName());
      assertEquals("Four legged llama", projectEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testRemoveProjectTag() throws ClientProtocolException, IOException {
    String path = "/projects/projects/1/tags/1";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals(0, projectEntity.getTags_ids().size());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // ReportRESTServiceTests

  @Test
  public void testCreateReport() throws ClientProtocolException, IOException {
    ReportEntity reportEntity = new ReportEntity();
    reportEntity.setName("Pyramus RESTTest Report");
    reportEntity.setData("llama data");
    
    HttpResponse response = doPostRequest("/reports/reports", reportEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals("Pyramus RESTTest Report", reportEntity.getName());
      assertEquals("llama data", reportEntity.getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateReportCategory() throws ClientProtocolException, IOException {
    ReportCategoryEntity reportCategoryEntity = new ReportCategoryEntity();
    reportCategoryEntity.setName("Llama category");
    reportCategoryEntity.setIndexColumn(1);

    HttpResponse response = doPostRequest("/reports/categories", reportCategoryEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals("Llama category", reportCategoryEntity.getName());
      assertEquals((Integer) 1, reportCategoryEntity.getIndexColumn());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindReports() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/reports/reports");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity[] reportEntities = unserializeEntity(ReportEntity[].class, EntityUtils.toString(entity));
      assertNotNull(reportEntities);
      assertEquals(1, reportEntities.length);
      assertEquals("Pyramus RESTTest Report", reportEntities[0].getName());
      assertEquals("llama data", reportEntities[0].getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindReportById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/reports/reports/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals((Long) 1l, reportEntity.getId());
      assertEquals("Pyramus RESTTest Report", reportEntity.getName());
      assertEquals("llama data", reportEntity.getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindReportCategories() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/reports/categories");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity[] reportCategoryEntities = unserializeEntity(ReportCategoryEntity[].class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntities);
      assertEquals(1, reportCategoryEntities.length);
      assertEquals("Llama category", reportCategoryEntities[0].getName());
      assertEquals((Integer) 1, reportCategoryEntities[0].getIndexColumn());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindReportCategoryById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/reports/categories/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals("Llama category", reportCategoryEntity.getName());
      assertEquals((Integer) 1, reportCategoryEntity.getIndexColumn());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateReport() throws ClientProtocolException, IOException {
    ReportEntity reportEntity = new ReportEntity();
    reportEntity.setName("Updated ReportREST");
    
    String path = "/reports/reports/1";

    HttpResponse response = doPutRequest(path, reportEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals((Long) 1l, reportEntity.getId());
      assertEquals("Updated ReportREST", reportEntity.getName());
      assertEquals("llama data", reportEntity.getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateReportCategory() throws ClientProtocolException, IOException {
    ReportCategoryEntity reportCategoryEntity = new ReportCategoryEntity();
    reportCategoryEntity.setName("Special Llamas");
    reportCategoryEntity.setIndexColumn(2);
    
    String path = "/reports/categories/1";

    HttpResponse response = doPutRequest(path, reportCategoryEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals((Long) 1l, reportCategoryEntity.getId());
      assertEquals("Special Llamas", reportCategoryEntity.getName());
      assertEquals((Integer) 2, reportCategoryEntity.getIndexColumn());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveReport() throws ClientProtocolException, IOException {
    String path = "/reports/reports/1";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals((Long) 1l, reportEntity.getId());
      assertEquals(true, reportEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveReport() throws ClientProtocolException, IOException {
    ReportEntity reportEntity = new ReportEntity();
    reportEntity.setArchived(false);
    
    String path = "/reports/reports/1";

    HttpResponse response = doPutRequest(path, reportEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals((Long) 1l, reportEntity.getId());
      assertEquals(false, reportEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveReportCategory() throws ClientProtocolException, IOException {
    String path = "/reports/categories/1";

    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals((Long) 1l, reportCategoryEntity.getId());
      assertEquals(true, reportCategoryEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveReportCategory() throws ClientProtocolException, IOException {
    ReportCategoryEntity reportCategoryEntity = new ReportCategoryEntity();
    reportCategoryEntity.setArchived(false);
    
    String path = "/reports/categories/1";

    HttpResponse response = doPutRequest(path, reportCategoryEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals((Long) 1l, reportCategoryEntity.getId());
      assertEquals(false, reportCategoryEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // TagRESTServiceTests

  @Test
  public void testCreateTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("Ratkaisutiimi");
    
    HttpResponse response = doPostRequest("/tags/tags/", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals("Ratkaisutiimi", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(2, tagEntities.length);
      assertEquals("Test environment", tagEntities[0].getText());
      assertEquals("Ratkaisutiimi", tagEntities[1].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindTagByText() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/?text=Ratkaisutiimi");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals("Ratkaisutiimi", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindTagById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 1l, tagEntity.getId());
      assertEquals("Test environment", tagEntity.getText());

    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindProjectsByTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("Test environment");

    doPostRequest("/projects/projects/1/tags", tagEntity);

    HttpResponse response = doGetRequest("/tags/tags/1/projects");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity[] projectEntities = unserializeEntity(ProjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectEntities);
      assertEquals(1, projectEntities.length);
      assertEquals("Updated project", projectEntities[0].getName());
      assertEquals("Four legged llama", projectEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("Updated tag");

    String path = "/tags/tags/1";

    HttpResponse response = doPutRequest(path, tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 1l, tagEntity.getId());
      assertEquals("Updated tag", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testDeleteTag() throws ClientProtocolException, IOException {
    String path = "/tags/tags/2";
    HttpResponse response = doDeleteRequest(path);

    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  // CommonRESTServiceTests EducationTypes

  @Test
  public void testCreateEducationType() throws ClientProtocolException, IOException {
    EducationTypeEntity educationTypeEntity = new EducationTypeEntity();
    educationTypeEntity.setName("Etaopinto");
    educationTypeEntity.setCode("EO1");

    HttpResponse response = doPostRequest("/common/educationTypes/", educationTypeEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntity);
      assertEquals("Etaopinto", educationTypeEntity.getName());
      assertEquals("EO1", educationTypeEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindEducationTypes() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/educationTypes/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity[] educationTypeEntities = unserializeEntity(EducationTypeEntity[].class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntities);
      assertEquals(1, educationTypeEntities.length);
      assertEquals("Etaopinto", educationTypeEntities[0].getName());
      assertEquals("EO1", educationTypeEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindEducationTypeById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/educationTypes/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntity);
      assertEquals((Long) 1l, educationTypeEntity.getId());
      assertEquals("Etaopinto", educationTypeEntity.getName());
      assertEquals("EO1", educationTypeEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateEducationType() throws ClientProtocolException, IOException {
    EducationTypeEntity educationTypeEntity = new EducationTypeEntity();
    educationTypeEntity.setName("LlamaTeaching");
    educationTypeEntity.setCode("Llama");

    HttpResponse response = doPutRequest("/common/educationTypes/1", educationTypeEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntity);
      assertEquals("LlamaTeaching", educationTypeEntity.getName());
      assertEquals("Llama", educationTypeEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveEducationType() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/common/educationTypes/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntity);
      assertEquals((Long) 1l, educationTypeEntity.getId());
      assertEquals(true, educationTypeEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveEducationType() throws ClientProtocolException, IOException {
    EducationTypeEntity educationTypeEntity = new EducationTypeEntity();
    educationTypeEntity.setArchived(false);
    
    HttpResponse response = doPutRequest("/common/educationTypes/1", educationTypeEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationTypeEntity);
      assertEquals((Long) 1l, educationTypeEntity.getId());
      assertEquals(false, educationTypeEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // CommonRESTServiceTests Subjects

  @Test
  public void testCreateSubject() throws ClientProtocolException, IOException {
    SubjectEntity subjectEntity = new SubjectEntity();
    subjectEntity.setName("Basic Math");
    subjectEntity.setCode("BM1");
    subjectEntity.setEducationType_id(1l);

    HttpResponse response = doPostRequest("/common/subjects/", subjectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals("Basic Math", subjectEntity.getName());
      assertEquals("BM1", subjectEntity.getCode());
      assertEquals((Long) 1l, subjectEntity.getEducationType_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSubjects() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/subjects/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity[] subjectEntities = unserializeEntity(SubjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(subjectEntities);
      assertEquals(1, subjectEntities.length);
      assertEquals("Basic Math", subjectEntities[0].getName());
      assertEquals("BM1", subjectEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSubjectById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/subjects/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals((Long) 1l, subjectEntity.getId());
      assertEquals("Basic Math", subjectEntity.getName());
      assertEquals("BM1", subjectEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindSubjectsByEducationType() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/educationTypes/1/subjects");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity[] subjectEntities = unserializeEntity(SubjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(subjectEntities);
      assertEquals(1, subjectEntities.length);
      assertEquals("Basic Math", subjectEntities[0].getName());
      assertEquals("BM1", subjectEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateSubject() throws ClientProtocolException, IOException {
    SubjectEntity subjectEntity = new SubjectEntity();
    subjectEntity.setName("Llama Math");
    subjectEntity.setCode("LM1");
    subjectEntity.setEducationType_id(1l);
    
    HttpResponse response = doPutRequest("/common/subjects/1", subjectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals("Llama Math", subjectEntity.getName());
      assertEquals("LM1", subjectEntity.getCode());
      assertEquals((Long) 1l, subjectEntity.getEducationType_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveSubject() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/common/subjects/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals((Long) 1l, subjectEntity.getId());
      assertEquals(true, subjectEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveSubject() throws ClientProtocolException, IOException {
    SubjectEntity subjectEntity = new SubjectEntity();
    subjectEntity.setArchived(false);

    HttpResponse response = doPutRequest("/common/subjects/1", subjectEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals((Long) 1l, subjectEntity.getId());
      assertEquals(false, subjectEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testSearchSubjects() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/subjects/?text=Llama");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity[] subjectEntities = unserializeEntity(SubjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(subjectEntities);
      assertEquals(1, subjectEntities.length);
      assertEquals("Llama Math", subjectEntities[0].getName());
      assertEquals("LM1", subjectEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // CommonRESTServiceTests GradingScales

  @Test
  public void testCreateGradingScale() throws ClientProtocolException, IOException {
    GradingScaleEntity gradingScaleEntity = new GradingScaleEntity();
    gradingScaleEntity.setName("Standard Grading");
    gradingScaleEntity.setDescription("Basic gradingScale");

    HttpResponse response = doPostRequest("/common/gradingScales/", gradingScaleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      gradingScaleEntity = unserializeEntity(GradingScaleEntity.class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntity);
      assertEquals("Standard Grading", gradingScaleEntity.getName());
      assertEquals("Basic gradingScale", gradingScaleEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindGradingScales() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/gradingScales/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      GradingScaleEntity[] gradingScaleEntities = unserializeEntity(GradingScaleEntity[].class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntities);
      assertEquals(1, gradingScaleEntities.length);
      assertEquals("Standard Grading", gradingScaleEntities[0].getName());
      assertEquals("Basic gradingScale", gradingScaleEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindGradingScaleById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/gradingScales/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      GradingScaleEntity gradingScaleEntity = unserializeEntity(GradingScaleEntity.class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntity);
      assertEquals((Long) 1l, gradingScaleEntity.getId());
      assertEquals("Standard Grading", gradingScaleEntity.getName());
      assertEquals("Basic gradingScale", gradingScaleEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateGradingScale() throws ClientProtocolException, IOException {
    GradingScaleEntity gradingScaleEntity = new GradingScaleEntity();
    gradingScaleEntity.setName("Llama Grading");
    gradingScaleEntity.setDescription("GradingScale for llamas");

    HttpResponse response = doPutRequest("/common/gradingScales/1", gradingScaleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      gradingScaleEntity = unserializeEntity(GradingScaleEntity.class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntity);
      assertEquals("Llama Grading", gradingScaleEntity.getName());
      assertEquals("GradingScale for llamas", gradingScaleEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveGradingScale() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/common/gradingScales/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      GradingScaleEntity gradingScaleEntity = unserializeEntity(GradingScaleEntity.class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntity);
      assertEquals((Long) 1l, gradingScaleEntity.getId());
      assertEquals(true, gradingScaleEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveGradingScale() throws ClientProtocolException, IOException {
    GradingScaleEntity gradingScaleEntity = new GradingScaleEntity();
    gradingScaleEntity.setArchived(false);
    
    HttpResponse response = doPutRequest("/common/gradingScales/1", gradingScaleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      gradingScaleEntity = unserializeEntity(GradingScaleEntity.class, EntityUtils.toString(entity));
      assertNotNull(gradingScaleEntity);
      assertEquals((Long) 1l, gradingScaleEntity.getId());
      assertEquals(false, gradingScaleEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // CommonRESTServiceTests EducationalTimeUnits

  @Test
  public void testCreateEducationalTimeUnit() throws ClientProtocolException, IOException {
    EducationalTimeUnitEntity educationalTimeUnitEntity = new EducationalTimeUnitEntity();
    educationalTimeUnitEntity.setName("Standard timeUnit");
    educationalTimeUnitEntity.setBaseUnits(60d);

    HttpResponse response = doPostRequest("/common/educationalTimeUnits/", educationalTimeUnitEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationalTimeUnitEntity = unserializeEntity(EducationalTimeUnitEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntity);
      assertEquals("Standard timeUnit", educationalTimeUnitEntity.getName());
      assertEquals((Double) 60d, educationalTimeUnitEntity.getBaseUnits());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindEducationalTimeUnits() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/educationalTimeUnits/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationalTimeUnitEntity[] educationalTimeUnitEntities = unserializeEntity(EducationalTimeUnitEntity[].class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntities);
      assertEquals(1, educationalTimeUnitEntities.length);
      assertEquals("Standard timeUnit", educationalTimeUnitEntities[0].getName());
      assertEquals((Double) 60d, educationalTimeUnitEntities[0].getBaseUnits());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindEducationalTimeUnitById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/common/educationalTimeUnits/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationalTimeUnitEntity educationalTimeUnitEntity = unserializeEntity(EducationalTimeUnitEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntity);
      assertEquals((Long) 1l, educationalTimeUnitEntity.getId());
      assertEquals("Standard timeUnit", educationalTimeUnitEntity.getName());
      assertEquals((Double) 60d, educationalTimeUnitEntity.getBaseUnits());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateEducationalTimeUnit() throws ClientProtocolException, IOException {
    EducationalTimeUnitEntity educationalTimeUnitEntity = new EducationalTimeUnitEntity();
    educationalTimeUnitEntity.setName("Llama Time");
    educationalTimeUnitEntity.setBaseUnits(4d);
    
    HttpResponse response = doPutRequest("/common/educationalTimeUnits/1", educationalTimeUnitEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationalTimeUnitEntity = unserializeEntity(EducationalTimeUnitEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntity);
      assertEquals("Llama Time", educationalTimeUnitEntity.getName());
      assertEquals((Double) 4d, educationalTimeUnitEntity.getBaseUnits());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveEducationalTimeUnit() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/common/educationalTimeUnits/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationalTimeUnitEntity educationalTimeUnitEntity = unserializeEntity(EducationalTimeUnitEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntity);
      assertEquals((Long) 1l, educationalTimeUnitEntity.getId());
      assertEquals(true, educationalTimeUnitEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveEducationalTimeUnit() throws ClientProtocolException, IOException {
    EducationalTimeUnitEntity educationalTimeUnitEntity = new EducationalTimeUnitEntity();
    educationalTimeUnitEntity.setArchived(false);

    HttpResponse response = doPutRequest("/common/educationalTimeUnits/1", educationalTimeUnitEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationalTimeUnitEntity = unserializeEntity(EducationalTimeUnitEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalTimeUnitEntity);
      assertEquals((Long) 1l, educationalTimeUnitEntity.getId());
      assertEquals(false, educationalTimeUnitEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  // ModuleRESTServiceTests

  @Test
  public void testCreateModule() throws ClientProtocolException, IOException {
    ModuleEntity moduleEntity = new ModuleEntity();
    moduleEntity.setName("Basic module");
    moduleEntity.setSubject_id(1l);
    moduleEntity.setCourseNumber(1);
    moduleEntity.setCourseLength_id(1l);
    moduleEntity.setDescription("module for unitTesting");
    moduleEntity.setMaxParticipantCount(20l);

    HttpResponse response = doPostRequest("/modules/modules/", moduleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      moduleEntity = unserializeEntity(ModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(moduleEntity);
      assertEquals("Basic module", moduleEntity.getName());
      assertEquals((Long) 1l, moduleEntity.getSubject_id());
      assertEquals((Integer) 1, moduleEntity.getCourseNumber());
      assertEquals("module for unitTesting", moduleEntity.getDescription());
      assertEquals((Long) 20l, moduleEntity.getMaxParticipantCount());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindModules() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/modules/modules/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ModuleEntity[] moduleEntities = unserializeEntity(ModuleEntity[].class, EntityUtils.toString(entity));
      assertNotNull(moduleEntities);
      assertEquals(1, moduleEntities.length);
      assertEquals("Basic module", moduleEntities[0].getName());
      assertEquals("module for unitTesting", moduleEntities[0].getDescription());
      assertEquals((Long) 20l, moduleEntities[0].getMaxParticipantCount());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindModuleById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/modules/modules/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ModuleEntity moduleEntity = unserializeEntity(ModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(moduleEntity);
      assertEquals((Long) 1l, moduleEntity.getId());
      assertEquals("Basic module", moduleEntity.getName());
      assertEquals("module for unitTesting", moduleEntity.getDescription());
      assertEquals((Long) 20l, moduleEntity.getMaxParticipantCount());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateModule() throws ClientProtocolException, IOException {
    ModuleEntity moduleEntity = new ModuleEntity();
    moduleEntity.setName("Llama module");
    moduleEntity.setSubject_id(1l);
    moduleEntity.setCourseNumber(1);
    moduleEntity.setCourseLength_id(1l);
    moduleEntity.setDescription("module for llamas");
    moduleEntity.setMaxParticipantCount(12l);

    HttpResponse response = doPutRequest("/modules/modules/1", moduleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      moduleEntity = unserializeEntity(ModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(moduleEntity);
      assertEquals("Llama module", moduleEntity.getName());
      assertEquals((Long) 1l, moduleEntity.getSubject_id());
      assertEquals((Integer) 1, moduleEntity.getCourseNumber());
      assertEquals("module for llamas", moduleEntity.getDescription());
      assertEquals((Long) 12l, moduleEntity.getMaxParticipantCount());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveModule() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/modules/modules/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ModuleEntity moduleEntity = unserializeEntity(ModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(moduleEntity);
      assertEquals((Long) 1l, moduleEntity.getId());
      assertEquals(true, moduleEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveModule() throws ClientProtocolException, IOException {
    ModuleEntity moduleEntity = new ModuleEntity();
    moduleEntity.setArchived(false);

    HttpResponse response = doPutRequest("/modules/modules/1", moduleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      moduleEntity = unserializeEntity(ModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(moduleEntity);
      assertEquals((Long) 1l, moduleEntity.getId());
      assertEquals(false, moduleEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // ModuleRESTServiceTests Tags
  
  @Test
  public void testCreateModuleTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("Module Tag");

    HttpResponse response = doPostRequest("/modules/modules/1/tags", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 3l, tagEntity.getId());
      assertEquals("Module Tag", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindModuleTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/modules/modules/1/tags");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(1, tagEntities.length);
      assertEquals("Module Tag", tagEntities[0].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindModulesByTag() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/3/modules");
    
    assertEquals(200, response.getStatusLine().getStatusCode());
    
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ModuleEntity[] moduleEntities = unserializeEntity(ModuleEntity[].class,EntityUtils.toString(entity));
      assertNotNull(moduleEntities);
      assertEquals(1, moduleEntities.length);
      assertEquals((Long) 1l, moduleEntities[0].getId());
      assertEquals("Llama module", moduleEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testRemoveTag() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/modules/modules/1/tags/3");

    assertEquals(200, response.getStatusLine().getStatusCode());
  }
  
  // ProjectRESTServiceTests ProjectModule
  
  @Test
  public void testCreateProjectModule() throws ClientProtocolException, IOException {
    ProjectModuleEntity projectModuleEntity = new ProjectModuleEntity();
    projectModuleEntity.setModule_id(1l);
    projectModuleEntity.setOptionality(ProjectModuleOptionality.getOptionality(1));

    HttpResponse response = doPostRequest("/projects/projects/1/modules/", projectModuleEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      projectModuleEntity = unserializeEntity(ProjectModuleEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectModuleEntity);
      assertEquals((Long) 1l, projectModuleEntity.getProject_id());
      assertEquals((Long) 1l, projectModuleEntity.getModule_id());
      assertEquals("OPTIONAL", projectModuleEntity.getOptionality().toString());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindProjectModules() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/projects/projects/1/modules/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectModuleEntity[] projectModuleEntities = unserializeEntity(ProjectModuleEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectModuleEntities);
      assertEquals(1, projectModuleEntities.length);
      assertEquals((Long) 1l, projectModuleEntities[0].getProject_id());
      assertEquals((Long) 1l, projectModuleEntities[0].getModule_id());
      assertEquals("OPTIONAL", projectModuleEntities[0].getOptionality().toString());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindProjectsByModule() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/modules/modules/1/projects");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity[] projectEntities = unserializeEntity(ProjectEntity[].class, EntityUtils.toString(entity));
      assertNotNull(projectEntities);
      assertEquals(1, projectEntities.length);
      assertEquals("Updated project", projectEntities[0].getName());
      assertEquals("Four legged llama", projectEntities[0].getDescription());
      assertEquals((Long) 1l,projectEntities[0].getId());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testDeleteProjectModule() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/projects/projects/1/modules/1");

    assertEquals(200, response.getStatusLine().getStatusCode());
  }
  
  // CalendarRESTServiceTests acedemicTerm
  
  @Test
  public void testCreateAcademicTerm() throws ClientProtocolException, IOException, ParseException {
    AcademicTermEntity academicTermEntity = new AcademicTermEntity();
    academicTermEntity.setName("Test term");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = sdf.parse("2012-04-23");
    Date endDate = sdf.parse("2012-07-27");
    academicTermEntity.setStartDate(startDate);
    academicTermEntity.setEndDate(endDate);

    HttpResponse response = doPostRequest("/calendar/academicTerms/", academicTermEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      academicTermEntity = unserializeEntity(AcademicTermEntity.class, EntityUtils.toString(entity));
      assertNotNull(academicTermEntity);
      assertEquals("Test term", academicTermEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2012-04-23T00:00:00").getTime(), academicTermEntity.getStartDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2012-07-27T00:00:00").getTime(), academicTermEntity.getEndDate());

    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindAcademicTerms() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/calendar/academicTerms/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AcademicTermEntity[] academicTermsEntities = unserializeEntity(AcademicTermEntity[].class, EntityUtils.toString(entity));
      assertNotNull(academicTermsEntities);
      assertEquals(1, academicTermsEntities.length);
      assertEquals("Test term", academicTermsEntities[0].getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2012-04-23T00:00:00").getTime(), academicTermsEntities[0].getStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindAcademicTermById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/calendar/academicTerms/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AcademicTermEntity academicTermEntity = unserializeEntity(AcademicTermEntity.class, EntityUtils.toString(entity));
      assertNotNull(academicTermEntity);
      assertEquals((Long) 1l, academicTermEntity.getId());
      assertEquals("Test term", academicTermEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2012-04-23T00:00:00").getTime(), academicTermEntity.getStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateAcademicTerm() throws ClientProtocolException, IOException, ParseException {
    AcademicTermEntity academicTermEntity = new AcademicTermEntity();
    academicTermEntity.setName("Llama term");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = sdf.parse("2013-01-23");
    Date endDate = sdf.parse("2013-05-31");
    academicTermEntity.setStartDate(startDate);
    academicTermEntity.setEndDate(endDate);

    HttpResponse response = doPutRequest("/calendar/academicTerms/1", academicTermEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      academicTermEntity = unserializeEntity(AcademicTermEntity.class, EntityUtils.toString(entity));
      assertNotNull(academicTermEntity);
      assertEquals("Llama term", academicTermEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-01-23T00:00:00").getTime(), academicTermEntity.getStartDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-05-31T00:00:00").getTime(), academicTermEntity.getEndDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveAcademicTerm() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/calendar/academicTerms/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AcademicTermEntity academicTermEntity = unserializeEntity(AcademicTermEntity.class, EntityUtils.toString(entity));
      assertNotNull(academicTermEntity);
      assertEquals((Long) 1l, academicTermEntity.getId());
      assertEquals(true, academicTermEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUnarchiveAcademicTerm() throws ClientProtocolException, IOException {
    AcademicTermEntity academicTermEntity = new AcademicTermEntity();
    academicTermEntity.setArchived(false);

    HttpResponse response = doPutRequest("/calendar/academicTerms/1", academicTermEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      academicTermEntity = unserializeEntity(AcademicTermEntity.class, EntityUtils.toString(entity));
      assertNotNull(academicTermEntity);
      assertEquals((Long) 1l, academicTermEntity.getId());
      assertEquals(false, academicTermEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }  
}
