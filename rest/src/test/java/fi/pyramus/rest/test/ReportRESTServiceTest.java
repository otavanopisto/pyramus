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

import fi.pyramus.rest.ReportRESTService;
import fi.pyramus.rest.controller.ReportController;
import fi.pyramus.rest.tranquil.reports.ReportCategoryEntity;
import fi.pyramus.rest.tranquil.reports.ReportEntity;

@RunWith(Arquillian.class)
public class ReportRESTServiceTest extends RestfulServiceTest{

  public ReportRESTServiceTest() throws URISyntaxException {
    super();
  }
  
  @Deployment
  @OverProtocol("Servlet 3.0")
  public static Archive<?> createTestArchive() {
    Archive<?> archive = createArchive(ReportController.class, ReportRESTService.class);
    return archive;
  }

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
  
  public void testUpdateReport() throws ClientProtocolException, IOException {

    String path = "/reports/reports/1";

    StringEntity str = new StringEntity("{\"name\":\"Updated ReportREST\",\"data\":\"rideableLlama\"}");

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
      assertEquals("rideableLlama", reportEntity.getData());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  public void testUpdateReportCategory() throws ClientProtocolException, IOException {

    String path = "/reportCategorys/reportCategorys/1";

    StringEntity str = new StringEntity("{\"name\":\"Speacial Llamas\",\"indexColumn\":2}");

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
    StringEntity str = new StringEntity("{\"archived\":true}");

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
    StringEntity str = new StringEntity("{\"archived\":true}");

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
}
