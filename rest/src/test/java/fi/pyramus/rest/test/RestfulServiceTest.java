package fi.pyramus.rest.test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import fi.pyramus.rest.AbstractRESTService;
import fi.pyramus.rest.JaxRsActivator;
import fi.pyramus.rest.TranquilityBuilderFactoryProvider;

public class RestfulServiceTest {

  public RestfulServiceTest() throws URISyntaxException {
    baseUri = new URI("http://localhost:8080/test/1");
  }

  protected static Archive<?> createArchive(Class<?>... testClasses) {
    MavenDependencyResolver resolver = DependencyResolvers
        .use(MavenDependencyResolver.class)
        .loadMetadataFromPom("pom.xml");

    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addAsLibraries(getHibernateSearchFiles(resolver))
        .addPackages(true, getPersistencePackages())
        .addClasses(testClasses)
        .addClasses(AbstractRESTService.class, TranquilityBuilderFactoryProvider.class, JaxRsActivator.class)
        .addClasses(org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver.class, RestfulServiceTest.class)
        .addAsLibraries(resolver.artifact("fi.tranquil-model:tranquil-model:1.0.3-SNAPSHOT").resolveAsFiles())
        .addAsLibraries(resolver.artifact("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-impl-maven:1.0.0-beta-7").resolveAsFiles())
        .addAsLibraries(resolver.artifact("org.apache.httpcomponents:httpcore:4.1.2").resolveAsFiles())
        .addAsLibraries(resolver.artifact("org.apache.httpcomponents:httpclient:4.1.2").resolveAsFiles())
        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource("test-ds.xml");
  }

  protected static Package[] getPersistencePackages() {
    Package domainModelPackage = fi.pyramus.domainmodel.DomainModelExtensionDescriptor.class.getPackage();
    Package daoPackage = fi.pyramus.dao.GenericDAO.class.getPackage();
    Package persistenceSearchPackage = fi.pyramus.persistence.search.SearchResult.class.getPackage();
    Package persistenceUserTypesPackage = fi.pyramus.persistence.usertypes.MonetaryAmount.class.getPackage();
    Package tranquilPackage = fi.pyramus.rest.tranquil.EntityLookup.class.getPackage();

    return new Package[] { domainModelPackage, daoPackage, persistenceSearchPackage, persistenceUserTypesPackage, tranquilPackage };
  }

  protected static File[] getHibernateSearchFiles(MavenDependencyResolver resolver) {
    File[] hibernateSearchFiles = resolver.artifact("org.hibernate:hibernate-search:4.1.0.CR3").exclusions("org.hibernate:hibernate-core:*").resolveAsFiles();
    File[] luceneQueriesFiles = resolver.artifact("org.apache.lucene:lucene-queries:3.5.0").resolveAsFiles();

    return ArrayUtils.addAll(hibernateSearchFiles, luceneQueriesFiles);
  }

  protected static void log(String message) {
    System.out.println(message);
  }

  protected String serializeEntity(Object entity) throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(entity);
  }

  protected <T> T unserializeEntity(Class<? extends T> clazz, String data) throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    StringReader stringReader = new StringReader(data);
    try {
      return mapper.readValue(stringReader, clazz);
    } finally {
      stringReader.close();
    }
  }

  protected HttpResponse doPostRequest(String path, StringEntity entity) throws ClientProtocolException, IOException {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpPost request = new HttpPost(baseUri + path);
    // String serialized = serializeEntity(entity);
    // request.setEntity(new StringEntity(serialized, "application/json", "UTF-8"));
    request.addHeader("content-type", "application/json");
    request.setEntity(entity);
    return httpClient.execute(request);
  }

  protected HttpResponse doGetRequest(String path) throws ClientProtocolException, IOException {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpGet request = new HttpGet(baseUri + path);
    return httpClient.execute(request);
  }

  protected HttpResponse doDeleteRequest(String path) throws ClientProtocolException, IOException {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpDelete request = new HttpDelete(baseUri + path);
    return httpClient.execute(request);
  }

  private URI baseUri;
}
