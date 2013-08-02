package fi.pyramus.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import fi.pyramus.rest.SchoolRESTService;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.tranquil.base.SchoolEntity;
import fi.pyramus.rest.tranquil.base.SchoolFieldEntity;
import fi.pyramus.rest.tranquil.base.SchoolVariableEntity;

@RunWith(Arquillian.class)
public class SchoolRESTServiceTest extends RestfulServiceTest {

  @Inject
  private SchoolController schoolController;

  @Inject
  private InitialSchoolDataDescriptor initialSchoolDataDescriptor;

  public SchoolRESTServiceTest() throws URISyntaxException {
    super();
  }

  @Deployment
  @OverProtocol("Servlet 3.0")
  public static Archive<?> createTestArchive() {
    Archive<?> archive = createArchive(InitialSchoolDataDescriptor.class, SchoolController.class, SchoolRESTService.class);

    return archive;
  }

  @Test
  public void testSanity() {
    assertNotNull(schoolController);
    School school = schoolController.findSchoolById(initialSchoolDataDescriptor.getSchoolId());
    assertNotNull(school);
    assertEquals(initialSchoolDataDescriptor.getSchoolId(), school.getId());
  }

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
  public void testSchoolFindVariableByID() throws ClientProtocolException, IOException {
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
    StringEntity str = new StringEntity("");
    HttpResponse response = doPostRequest(path,str);

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
    StringEntity str = new StringEntity("");
    HttpResponse response = doPostRequest(path,str);

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

}
