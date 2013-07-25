package fi.pyramus.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

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
import fi.pyramus.rest.SchoolRESTService;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.tranquil.base.SchoolEntity;

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
		Archive<?> archive = createArchive(
 		  InitialSchoolDataDescriptor.class,
			SchoolController.class,
		  SchoolRESTService.class
		);
		
		return archive;
	}	
	
	@Test
	public void testSanity() {
		assertNotNull(schoolController);
		School school = schoolController.getSchoolById(initialSchoolDataDescriptor.getSchoolId());
		assertNotNull(school);
		assertEquals(initialSchoolDataDescriptor.getSchoolId(), school.getId());
  }
	

	@Test
	public void testCreateSchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSchoolField() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSchools() throws ClientProtocolException, IOException {
		HttpResponse response = doGetRequest("/schools/schools");
		
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		HttpEntity entity = response.getEntity();
		try {
  		assertNotNull(entity);
  		assertEquals("application/json", entity.getContentType().getValue());
	  	SchoolEntity[] schoolEntities = unserializeEntity(SchoolEntity[].class, EntityUtils.toString(entity));
  		assertNotNull(schoolEntities);
  		assertEquals(1, schoolEntities.length);
  		assertEquals(initialSchoolDataDescriptor.getSchoolId(), schoolEntities[0].getId());
  		// TODO: Name, Code tests
		} finally {
			EntityUtils.consume(entity);
		}
	}

	@Test
	public void testGetSchoolById() throws ClientProtocolException, IOException {
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
		} finally {
			EntityUtils.consume(entity);
		}
	}

	@Test
	public void testGetSchoolVariablesBySchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSchoolFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSchoolFieldByID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVariables() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVariablesdByID() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateSchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateSchoolField() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateSchoolVariable() {
		fail("Not yet implemented");
	}

	@Test
	public void testArchiveSchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testArchiveSchoolField() {
		fail("Not yet implemented");
	}

}
