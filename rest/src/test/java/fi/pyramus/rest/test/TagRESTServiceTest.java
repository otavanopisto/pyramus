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

import fi.pyramus.rest.TagRESTService;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.TagEntity;

@RunWith(Arquillian.class)
public class TagRESTServiceTest extends RestfulServiceTest {

  public TagRESTServiceTest() throws URISyntaxException {
    super();
  }

  @Deployment
  @OverProtocol("Servlet 3.0")
  public static Archive<?> createTestArchive() {
    Archive<?> archive = createArchive(TagController.class, TagRESTService.class);
    return archive;
  }

  @Test
  public void testCreateTag() throws ClientProtocolException, IOException {
    StringEntity str = new StringEntity("{\"text\":\"Test environment\"}");

    HttpResponse response = doPostRequest("/tags/tags/", str);

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
  public void testFindTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/");

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
  }}
