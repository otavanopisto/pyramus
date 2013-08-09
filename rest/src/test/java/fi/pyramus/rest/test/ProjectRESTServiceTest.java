package fi.pyramus.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

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

import fi.pyramus.rest.ProjectRESTService;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.tranquil.projects.ProjectEntity;

@RunWith(Arquillian.class)
public class ProjectRESTServiceTest extends RestfulServiceTest {

  public ProjectRESTServiceTest() throws URISyntaxException {
    super();
  }

  @Deployment
  @OverProtocol("Servlet 3.0")
  public static Archive<?> createTestArchive() {
    Archive<?> archive = createArchive(ProjectController.class, ProjectRESTService.class);
    return archive;
  }

  @Test
  public void testCreateProject() throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"name\":\"Pyramus REST\", \"description\":\"Pyramus RESTService development\"}");

    HttpResponse response = doPostRequest("/projects/projects", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals("Pyramus REST", projectEntity.getName());
      assertEquals("Pyramus RESTService development", projectEntity.getDescription());
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
      assertEquals("Pyramus REST", projectEntities[0].getName());
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
      assertEquals("Pyramus REST", projectEntity.getName());
      assertEquals("Pyramus RESTService development", projectEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateProject() throws ClientProtocolException, IOException {

    String path = "/projects/projects/1";

    StringEntity str = new StringEntity("{\"name\":\"Updating project\",\"description\":\"Testing if project update works\"}");

    HttpResponse response = doPutRequest(path, str);

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ProjectEntity projectEntity = unserializeEntity(ProjectEntity.class, EntityUtils.toString(entity));
      assertNotNull(projectEntity);
      assertEquals((Long) 1l, projectEntity.getId());
      assertEquals("Updating project", projectEntity.getName());
      assertEquals("Testing if project update works", projectEntity.getDescription());
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
    StringEntity str = new StringEntity("{\"archived\":true}");

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
      assertEquals("Updating project", projectEntities[0].getName());
      assertEquals("Testing if project update works", projectEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }
}
