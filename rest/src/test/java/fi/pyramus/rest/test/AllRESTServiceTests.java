package fi.pyramus.rest.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.pyramus.domainmodel.base.School;
import fi.pyramus.rest.CommonRESTService;
import fi.pyramus.rest.ProjectRESTService;
import fi.pyramus.rest.ReportRESTService;
import fi.pyramus.rest.SchoolRESTService;
import fi.pyramus.rest.TagRESTService;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.controller.ReportController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.EducationTypeEntity;
import fi.pyramus.rest.tranquil.base.SchoolEntity;
import fi.pyramus.rest.tranquil.base.SchoolFieldEntity;
import fi.pyramus.rest.tranquil.base.SchoolVariableEntity;
import fi.pyramus.rest.tranquil.base.SubjectEntity;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.projects.ProjectEntity;
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
        CommonRESTService.class);

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
    StringEntity str = new StringEntity("{\"field_id\":1,\"code\":\"TAMK\",\"name\":\"Tampereen ammattikorkeakoulu\"}");

    HttpResponse response = doPostRequest("/schools/schools/", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"Polytechnic\"}");

    HttpResponse response = doPostRequest("/schools/schoolFields/", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals("Polytechnic", schoolFieldEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateSchoolVariable() throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"key_id\":1,\"school_id\":1,\"value\":\"Test variable\"}");

    HttpResponse response = doPostRequest("/schools/variables/", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
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
  public void testFindSchoolVariableBySchool() throws ClientProtocolException, IOException {
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

    String path = "/schools/schools/1";

    StringEntity str = new StringEntity("{\"field_id\":2,\"code\":\"SUST\",\"name\":\"Shahjalal University of Science and Technology\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
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
    String path = "/schools/schoolFields/1";

    StringEntity str = new StringEntity("{\"name\":\"College\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
      assertNotNull(schoolFieldEntity);
      assertEquals(initialSchoolDataDescriptor.getSchoolFieldId(), schoolFieldEntity.getId());
      assertEquals("College", schoolFieldEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateSchoolVariable() throws ClientProtocolException, IOException {
    String path = "/schools/variables/1";

    StringEntity str = new StringEntity("{\"value\":\"foo bar value\"}");

    HttpResponse response = doPutRequest(path, str);

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
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
    String path = "/schools/schools/2";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolEntity schoolEntity = unserializeEntity(SchoolEntity.class, EntityUtils.toString(entity));
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
    String path = "/schools/schoolFields/2";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolFieldEntity schoolFieldEntity = unserializeEntity(SchoolFieldEntity.class, EntityUtils.toString(entity));
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
    String path = "/schools/variables/1";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SchoolVariableEntity schoolVariableEntity = unserializeEntity(SchoolVariableEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"PyramusREST\", \"description\":\"Pyramus RESTService development\"}");

    HttpResponse response = doPostRequest("/projects/projects", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals("PyramusREST", projectEntity.getName());
      assertEquals("Pyramus RESTService development", projectEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateProjectTag() throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"text\":\"Test environment\"}");

    HttpResponse response = doPostRequest("/projects/projects/1/tags", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
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

    String path = "/projects/projects/1";

    StringEntity str = new StringEntity("{\"name\":\"Updated project\",\"description\":\"Four legged llama\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
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
    String path = "/projects/projects/1";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"Pyramus RESTTest Report\", \"data\":\"llama data\"}");

    HttpResponse response = doPostRequest("/reports/reports", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals("Pyramus RESTTest Report", reportEntity.getName());
      assertEquals("llama data", reportEntity.getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testCreateReportCategory() throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"name\":\"llama category\", \"indexColumn\":1}");

    HttpResponse response = doPostRequest("/reports/categories", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportCategoryEntity);
      assertEquals("llama category", reportCategoryEntity.getName());
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
      assertEquals("llama category", reportCategoryEntities[0].getName());
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
      assertEquals("llama category", reportCategoryEntity.getName());
      assertEquals((Integer) 1, reportCategoryEntity.getIndexColumn());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateReport() throws ClientProtocolException, IOException {

    String path = "/reports/reports/1";

    StringEntity str = new StringEntity("{\"name\":\"Updated ReportREST\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
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

    String path = "/reports/categories/1";

    StringEntity str = new StringEntity("{\"name\":\"Special Llamas\",\"indexColumn\":2}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
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
    String path = "/reports/reports/1";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
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
    String path = "/reports/categories/1";
    StringEntity str = new StringEntity("{\"archived\":false}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportCategoryEntity reportCategoryEntity = unserializeEntity(ReportCategoryEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"text\":\"Ratkaisutiimi\"}");

    HttpResponse response = doPostRequest("/tags/tags/", str);

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
  public void testFindProjectsByTag()  throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"text\":\"Test environment\"}");

    doPostRequest("/projects/projects/1/tags", str);

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

    String path = "/tags/tags/1";

    StringEntity str = new StringEntity("{\"text\":\"Updated tag\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"Etaopinto\",\"code\":\"EO1\"}");

    HttpResponse response = doPostRequest("/common/educationTypes/", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"LlamaTeaching\",\"code\":\"Llama\"}");

    HttpResponse response = doPutRequest("/common/educationTypes/1", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"archived\":false}");
    
    HttpResponse response = doPutRequest("/common/educationTypes/1", str);
    
    assertEquals(200, response.getStatusLine().getStatusCode());
    
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      EducationTypeEntity educationTypeEntity = unserializeEntity(EducationTypeEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"name\":\"Basic Math\",\"code\":\"BM1\", \"educationType_id\":1}");

    HttpResponse response = doPostRequest("/common/subjects/", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
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
  public void testFindSubjectId() throws ClientProtocolException, IOException {
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
    StringEntity str = new StringEntity("{\"name\":\"Llama Math\",\"code\":\"LM1\", \"educationType_id\":1}");

    HttpResponse response = doPutRequest("/common/subjects/1", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
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
    StringEntity str = new StringEntity("{\"archived\":false}");
    
    HttpResponse response = doPutRequest("/common/subjects/1", str);
    
    assertEquals(200, response.getStatusLine().getStatusCode());
    
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      SubjectEntity subjectEntity = unserializeEntity(SubjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(subjectEntity);
      assertEquals((Long) 1l, subjectEntity.getId());
      assertEquals(false, subjectEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
}
