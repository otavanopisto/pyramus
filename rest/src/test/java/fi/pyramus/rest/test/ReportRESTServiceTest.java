package fi.pyramus.rest.test;

import static org.junit.Assert.*;

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
    StringEntity str = new StringEntity("{\"name\":\"Pyramus RESTTest Report\", \"data\":\"laama data\"}");

    HttpResponse response = doPostRequest("/reports/reports", str);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      ReportEntity reportEntity = unserializeEntity(ReportEntity.class, EntityUtils.toString(entity));
      assertNotNull(reportEntity);
      assertEquals("Pyramus RESTTest Report", reportEntity.getName());
      assertEquals("laama data", reportEntity.getData());
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
      assertEquals("laama data", reportEntities[0].getData());
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
      assertEquals("laama data", reportEntity.getData());
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
}
