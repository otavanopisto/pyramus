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
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.rest.CalendarRESTService;
import fi.pyramus.rest.CommonRESTService;
import fi.pyramus.rest.CourseRESTService;
import fi.pyramus.rest.ModuleRESTService;
import fi.pyramus.rest.ProjectRESTService;
import fi.pyramus.rest.ReportRESTService;
import fi.pyramus.rest.SchoolRESTService;
import fi.pyramus.rest.StudentRESTService;
import fi.pyramus.rest.TagRESTService;
import fi.pyramus.rest.controller.AbstractStudentController;
import fi.pyramus.rest.controller.CalendarController;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.CourseController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.controller.ReportController;
import fi.pyramus.rest.controller.SchoolController;
import fi.pyramus.rest.controller.StudentController;
import fi.pyramus.rest.controller.StudentGroupController;
import fi.pyramus.rest.controller.StudentSubResourceController;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.AcademicTermEntity;
import fi.pyramus.rest.tranquil.base.EducationTypeEntity;
import fi.pyramus.rest.tranquil.base.EducationalTimeUnitEntity;
import fi.pyramus.rest.tranquil.base.LanguageEntity;
import fi.pyramus.rest.tranquil.base.MunicipalityEntity;
import fi.pyramus.rest.tranquil.base.NationalityEntity;
import fi.pyramus.rest.tranquil.base.SchoolEntity;
import fi.pyramus.rest.tranquil.base.SchoolFieldEntity;
import fi.pyramus.rest.tranquil.base.SchoolVariableEntity;
import fi.pyramus.rest.tranquil.base.StudyProgrammeCategoryEntity;
import fi.pyramus.rest.tranquil.base.StudyProgrammeEntity;
import fi.pyramus.rest.tranquil.base.SubjectEntity;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.courses.CourseComponentEntity;
import fi.pyramus.rest.tranquil.courses.CourseDescriptionCategoryEntity;
import fi.pyramus.rest.tranquil.courses.CourseEntity;
import fi.pyramus.rest.tranquil.courses.CourseParticipationTypeEntity;
import fi.pyramus.rest.tranquil.courses.CourseStateEntity;
import fi.pyramus.rest.tranquil.grading.GradingScaleEntity;
import fi.pyramus.rest.tranquil.modules.ModuleEntity;
import fi.pyramus.rest.tranquil.projects.ProjectEntity;
import fi.pyramus.rest.tranquil.projects.ProjectModuleEntity;
import fi.pyramus.rest.tranquil.reports.ReportCategoryEntity;
import fi.pyramus.rest.tranquil.reports.ReportEntity;
import fi.pyramus.rest.tranquil.students.AbstractStudentEntity;
import fi.pyramus.rest.tranquil.students.StudentActivityTypeEntity;
import fi.pyramus.rest.tranquil.students.StudentEducationalLevelEntity;
import fi.pyramus.rest.tranquil.students.StudentEntity;
import fi.pyramus.rest.tranquil.students.StudentExaminationTypeEntity;
import fi.pyramus.rest.tranquil.students.StudentGroupEntity;
import fi.pyramus.rest.tranquil.students.StudentStudyEndReasonEntity;

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
        CourseRESTService.class, AbstractStudentController.class, StudentSubResourceController.class, StudentController.class, StudentGroupController.class,
        StudentRESTService.class);

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
  public void testRemoveModuleTag() throws ClientProtocolException, IOException {
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
    Date startDate = sdf.parse("2013-01-01");
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
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-01-01T00:00:00").getTime(), academicTermEntity.getStartDate());
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
  
 //CourseRESTServiceTest CourseState
  

  @Test
  public void testCreateCourseState() throws ClientProtocolException, IOException, ParseException {
    CourseStateEntity courseStateEntity = new CourseStateEntity();
    courseStateEntity.setName("Test State");
  
    HttpResponse response = doPostRequest("/courses/courseStates/", courseStateEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseStateEntity = unserializeEntity(CourseStateEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntity);
      assertEquals((Long) 1l, courseStateEntity.getId());
      assertEquals("Test State", courseStateEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseStates() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courseStates/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseStateEntity[] courseStateEntities = unserializeEntity(CourseStateEntity[].class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntities);
      assertEquals(1, courseStateEntities.length);
      assertEquals("Test State", courseStateEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseStateById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courseStates/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseStateEntity courseStateEntity = unserializeEntity(CourseStateEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntity);
      assertEquals("Test State", courseStateEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateCourseState() throws ClientProtocolException, IOException, ParseException {
    CourseStateEntity courseStateEntity = new CourseStateEntity();
    courseStateEntity.setName("Updated State");
  
    HttpResponse response = doPutRequest("/courses/courseStates/1", courseStateEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseStateEntity = unserializeEntity(CourseStateEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntity);
      assertEquals("Updated State", courseStateEntity.getName());
      assertEquals(false, courseStateEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveCourseState() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/courseStates/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseStateEntity courseStateEntity = unserializeEntity(CourseStateEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntity);
      assertEquals((Long) 1l, courseStateEntity.getId());
      assertEquals(true, courseStateEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveCourseState() throws ClientProtocolException, IOException {
    CourseStateEntity courseStateEntity = new CourseStateEntity();
    courseStateEntity.setArchived(false);

    HttpResponse response = doPutRequest("/courses/courseStates/1", courseStateEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseStateEntity = unserializeEntity(CourseStateEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseStateEntity);
      assertEquals((Long) 1l, courseStateEntity.getId());
      assertEquals(false, courseStateEntity.getArchived());
      assertEquals("Updated State", courseStateEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //CourseRESTServiceTest CourseDescriptionCategory
  

  @Test
  public void testCreateCourseDescriptionCategory() throws ClientProtocolException, IOException, ParseException {
    CourseDescriptionCategoryEntity categoryEntity = new CourseDescriptionCategoryEntity();
    categoryEntity.setName("Test Category");
  
    HttpResponse response = doPostRequest("/courses/descriptionCategories/", categoryEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      categoryEntity = unserializeEntity(CourseDescriptionCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(categoryEntity);
      assertEquals((Long) 1l, categoryEntity.getId());
      assertEquals("Test Category", categoryEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseDescriptionCategories() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/descriptionCategories/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseDescriptionCategoryEntity[] categoryEntities = unserializeEntity(CourseDescriptionCategoryEntity[].class, EntityUtils.toString(entity));
      assertNotNull(categoryEntities);
      assertEquals(1, categoryEntities.length);
      assertEquals("Test Category", categoryEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseDescriptionCategoryById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/descriptionCategories/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseDescriptionCategoryEntity categoryEntity = unserializeEntity(CourseDescriptionCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(categoryEntity);
      assertEquals("Test Category", categoryEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateCourseDescriptionCategory() throws ClientProtocolException, IOException, ParseException {
    CourseDescriptionCategoryEntity categoryEntity = new CourseDescriptionCategoryEntity();
    categoryEntity.setName("Updated Category");
  
    HttpResponse response = doPutRequest("/courses/descriptionCategories/1", categoryEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      categoryEntity = unserializeEntity(CourseDescriptionCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(categoryEntity);
      assertEquals("Updated Category", categoryEntity.getName());
      assertEquals(false, categoryEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveCourseDescriptionCategory() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/descriptionCategories/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseDescriptionCategoryEntity categoriesEntity = unserializeEntity(CourseDescriptionCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(categoriesEntity);
      assertEquals((Long) 1l, categoriesEntity.getId());
      assertEquals(true, categoriesEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveCourseDescriptionCategory() throws ClientProtocolException, IOException {
    CourseDescriptionCategoryEntity categoryEntity = new CourseDescriptionCategoryEntity();
    categoryEntity.setArchived(false);

    HttpResponse response = doPutRequest("/courses/descriptionCategories/1", categoryEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      categoryEntity = unserializeEntity(CourseDescriptionCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(categoryEntity);
      assertEquals((Long) 1l, categoryEntity.getId());
      assertEquals(false, categoryEntity.getArchived());
      assertEquals("Updated Category", categoryEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //CourseRESTServiceTest CourseParticipationType

  @Test
  public void testCreateCourseParticipationType() throws ClientProtocolException, IOException, ParseException {
    CourseParticipationTypeEntity participationEntity = new CourseParticipationTypeEntity();
    participationEntity.setName("Test participationType");
  
    HttpResponse response = doPostRequest("/courses/participationTypes/", participationEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      participationEntity = unserializeEntity(CourseParticipationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(participationEntity);
      assertEquals((Long) 1l, participationEntity.getId());
      assertEquals("Test participationType", participationEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseParticipationTypes() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/participationTypes/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseParticipationTypeEntity[] participationTypeEntities = unserializeEntity(CourseParticipationTypeEntity[].class, EntityUtils.toString(entity));
      assertNotNull(participationTypeEntities);
      assertEquals(1, participationTypeEntities.length);
      assertEquals("Test participationType", participationTypeEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseParticipationTypeById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/participationTypes/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseParticipationTypeEntity participationTypeEntity = unserializeEntity(CourseParticipationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(participationTypeEntity);
      assertEquals("Test participationType", participationTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateCourseParticipationType() throws ClientProtocolException, IOException, ParseException {
    CourseParticipationTypeEntity participationTypeEntity = new CourseParticipationTypeEntity();
    participationTypeEntity.setName("Updated participationType");
  
    HttpResponse response = doPutRequest("/courses/participationTypes/1", participationTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      participationTypeEntity = unserializeEntity(CourseParticipationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(participationTypeEntity);
      assertEquals("Updated participationType", participationTypeEntity.getName());
      assertEquals(false, participationTypeEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveCourseParticipationType() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/participationTypes/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseParticipationTypeEntity participationTypeEntity = unserializeEntity(CourseParticipationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(participationTypeEntity);
      assertEquals((Long) 1l, participationTypeEntity.getId());
      assertEquals(true, participationTypeEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveCourseParticipationType() throws ClientProtocolException, IOException {
    CourseParticipationTypeEntity participationTypeEntity = new CourseParticipationTypeEntity();
    participationTypeEntity.setArchived(false);

    HttpResponse response = doPutRequest("/courses/participationTypes/1", participationTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      participationTypeEntity = unserializeEntity(CourseParticipationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(participationTypeEntity);
      assertEquals((Long) 1l, participationTypeEntity.getId());
      assertEquals(false, participationTypeEntity.getArchived());
      assertEquals("Updated participationType", participationTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // CourseRESTServiceTest Course
  
  @Test
  public void testCreateCourse() throws ClientProtocolException, IOException, ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = sdf.parse("2013-02-13");
    Date endDate = sdf.parse("2013-05-09");
    Date enrolmentTimeEnd = sdf.parse("2013-01-31");
    CourseEntity courseEntity = new CourseEntity();
    courseEntity.setModule_id(1l);
    courseEntity.setName("Test course");
    courseEntity.setNameExtension("Test extension");
    courseEntity.setState_id(1l);
    courseEntity.setSubject_id(1l);
    courseEntity.setCourseNumber(2);
    courseEntity.setBeginDate(beginDate);
    courseEntity.setEndDate(endDate);
    courseEntity.setCourseLength_id(1l);
    courseEntity.setCourseLength(15d);
    courseEntity.setDistanceTeachingDays(10d);
    courseEntity.setLocalTeachingDays(5d);
    courseEntity.setTeachingHours(60d);
    courseEntity.setPlanningHours(15d);
    courseEntity.setAssessingHours(5d);
    courseEntity.setDescription("Testing how courses work");
    courseEntity.setMaxParticipantCount(25l);
    courseEntity.setEnrolmentTimeEnd(enrolmentTimeEnd);
  
    HttpResponse response = doPostRequest("/courses/courses/", courseEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseEntity);
      assertEquals((Long) 1l, courseEntity.getId());
      assertEquals("Test course", courseEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntity.getBeginDate());
      assertEquals("Testing how courses work", courseEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourses() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courses/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity[] courseEntities = unserializeEntity(CourseEntity[].class, EntityUtils.toString(entity));
      assertNotNull(courseEntities);
      assertEquals(1, courseEntities.length);
      assertEquals("Test course", courseEntities[0].getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntities[0].getBeginDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-01-31T00:00:00").getTime(), courseEntities[0].getEnrolmentTimeEnd());
      assertEquals("Testing how courses work", courseEntities[0].getDescription());  
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courses/2");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseEntity);
      assertEquals("Test course", courseEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntity.getBeginDate());
      assertEquals("Testing how courses work", courseEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindCoursesByModule() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/modules/modules/1/courses");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity[] courseEntities = unserializeEntity(CourseEntity[].class, EntityUtils.toString(entity));
      assertNotNull(courseEntities);
      assertEquals(1, courseEntities.length);
      assertEquals("Test course", courseEntities[0].getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntities[0].getBeginDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-01-31T00:00:00").getTime(), courseEntities[0].getEnrolmentTimeEnd());
      assertEquals("Testing how courses work", courseEntities[0].getDescription());  
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindCoursesByTerm() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/calendar/academicTerms/1/courses");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity[] courseEntities = unserializeEntity(CourseEntity[].class, EntityUtils.toString(entity));
      assertNotNull(courseEntities);
      assertEquals(1, courseEntities.length);
      assertEquals("Test course", courseEntities[0].getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-02-13T00:00:00").getTime(), courseEntities[0].getBeginDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-01-31T00:00:00").getTime(), courseEntities[0].getEnrolmentTimeEnd());
      assertEquals("Testing how courses work", courseEntities[0].getDescription());  
    } finally {
      EntityUtils.consume(entity);
    }
  }
  

  @Test
  public void testUpdateCourse() throws ClientProtocolException, IOException, ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = sdf.parse("2013-08-04");
    Date endDate = sdf.parse("2013-12-20");
    Date enrolmentTimeEnd = sdf.parse("2013-07-02");
    CourseEntity courseEntity = new CourseEntity();
    courseEntity.setModule_id(1l);
    courseEntity.setName("Llama course");
    courseEntity.setNameExtension("extending");
    courseEntity.setState_id(1l);
    courseEntity.setSubject_id(1l);
    courseEntity.setCourseNumber(3);
    courseEntity.setBeginDate(beginDate);
    courseEntity.setEndDate(endDate);
    courseEntity.setCourseLength_id(1l);
    courseEntity.setCourseLength(20d);
    courseEntity.setDistanceTeachingDays(5d);
    courseEntity.setLocalTeachingDays(20d);
    courseEntity.setTeachingHours(80d);
    courseEntity.setPlanningHours(10d);
    courseEntity.setAssessingHours(2d);
    courseEntity.setDescription("Updating course");
    courseEntity.setMaxParticipantCount(30l);
    courseEntity.setEnrolmentTimeEnd(enrolmentTimeEnd);
  
    HttpResponse response = doPutRequest("/courses/courses/2", courseEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseEntity);
      assertEquals("Llama course", courseEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-08-04T00:00:00").getTime(), courseEntity.getBeginDate());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-12-20T00:00:00").getTime(), courseEntity.getEndDate());
      assertEquals(false, courseEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveCourse() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/courses/2");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseEntity);
      assertEquals((Long) 2l, courseEntity.getId());
      assertEquals(true, courseEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveCourse() throws ClientProtocolException, IOException {
    CourseEntity courseEntity = new CourseEntity();
    courseEntity.setArchived(false);

    HttpResponse response = doPutRequest("/courses/courses/2", courseEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      courseEntity = unserializeEntity(CourseEntity.class, EntityUtils.toString(entity));
      assertNotNull(courseEntity);
      assertEquals((Long) 2l, courseEntity.getId());
      assertEquals(false, courseEntity.getArchived());
      assertEquals("Llama course", courseEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-08-04T00:00:00").getTime(), courseEntity.getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //CourseRESTServiceTest CourseComponent

  @Test
  public void testCreateCourseComponent() throws ClientProtocolException, IOException, ParseException {
    CourseComponentEntity componentEntity = new CourseComponentEntity();
    componentEntity.setName("Test Component");
    componentEntity.setDescription("Courses have components");
    componentEntity.setLength(60d);
    componentEntity.setLength_id(1l);
  
    HttpResponse response = doPostRequest("/courses/courses/2/components", componentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      componentEntity = unserializeEntity(CourseComponentEntity.class, EntityUtils.toString(entity));
      assertNotNull(componentEntity);
      assertEquals((Long) 1l, componentEntity.getId());
      assertEquals("Test Component", componentEntity.getName());
      assertEquals("Courses have components", componentEntity.getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseComponents() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courses/2/components");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseComponentEntity[] componentEntities = unserializeEntity(CourseComponentEntity[].class, EntityUtils.toString(entity));
      assertNotNull(componentEntities);
      assertEquals(1, componentEntities.length);
      assertEquals((Long) 1l, componentEntities[0].getId());
      assertEquals("Test Component", componentEntities[0].getName());
      assertEquals("Courses have components", componentEntities[0].getDescription());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseComponentById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courses/2/components/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseComponentEntity componentEntity = unserializeEntity(CourseComponentEntity.class, EntityUtils.toString(entity));
      assertNotNull(componentEntity);
      assertEquals("Test Component", componentEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateCourseComponent() throws ClientProtocolException, IOException, ParseException {
    CourseComponentEntity componentEntity = new CourseComponentEntity();
    componentEntity.setName("Updated Component");
    componentEntity.setDescription("Llama has 4 legs and you can ride it!");
    componentEntity.setLength(9001d);
    componentEntity.setLength_id(1l);
  
    HttpResponse response = doPutRequest("/courses/courses/2/components/1", componentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      componentEntity = unserializeEntity(CourseComponentEntity.class, EntityUtils.toString(entity));
      assertNotNull(componentEntity);
      assertEquals("Updated Component", componentEntity.getName());
      assertEquals("Llama has 4 legs and you can ride it!", componentEntity.getDescription());
      assertEquals(false, componentEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testArchiveCourseComponent() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/courses/2/components/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseComponentEntity componentEntity = unserializeEntity(CourseComponentEntity.class, EntityUtils.toString(entity));
      assertNotNull(componentEntity);
      assertEquals((Long) 1l, componentEntity.getId());
      assertEquals(true, componentEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveCourseComponent() throws ClientProtocolException, IOException {
    CourseComponentEntity componentEntity = new CourseComponentEntity();
    componentEntity.setArchived(false);

    HttpResponse response = doPutRequest("/courses/courses/2/components/1", componentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      componentEntity = unserializeEntity(CourseComponentEntity.class, EntityUtils.toString(entity));
      assertNotNull(componentEntity);
      assertEquals((Long) 1l, componentEntity.getId());
      assertEquals(false, componentEntity.getArchived());
      assertEquals("Updated Component", componentEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //CourseRESTServiceTest Tag 

  @Test
  public void testCreateCourseTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("CourseTag");

    HttpResponse response = doPostRequest("/courses/courses/2/tags", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 4l, tagEntity.getId());
      assertEquals("CourseTag", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindCourseTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/courses/courses/2/tags");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(1, tagEntities.length);
      assertEquals("CourseTag", tagEntities[0].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindCoursesByTag() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/tags/tags/4/courses");
    
    assertEquals(200, response.getStatusLine().getStatusCode());
    
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      CourseEntity[] courseEntities = unserializeEntity(CourseEntity[].class, EntityUtils.toString(entity));
      assertNotNull(courseEntities);
      assertEquals(1, courseEntities.length);
      assertEquals(false, courseEntities[0].getArchived());
      assertEquals("Llama course", courseEntities[0].getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-08-04T00:00:00").getTime(), courseEntities[0].getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testRemoveCourseTag() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/courses/courses/2/tags/4");

    assertEquals(200, response.getStatusLine().getStatusCode());
  }
  
  
  // StudentRESTServiceTest AbstractStudent

  @Test
  public void testCreateAbstractStudent() throws ClientProtocolException, IOException, ParseException {
    AbstractStudentEntity abstractStudentEntity = new AbstractStudentEntity();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date birthday = sdf.parse("1981-02-19");
    abstractStudentEntity.setBirthday(birthday);
    abstractStudentEntity.setBasicInfo("Test BasicInfo");
    abstractStudentEntity.setSecureInfo(false);
    abstractStudentEntity.setSex(Sex.MALE);
    abstractStudentEntity.setSocialSecurityNumber("190281-116Y");
  
    HttpResponse response = doPostRequest("/students/abstractStudents/", abstractStudentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      abstractStudentEntity = unserializeEntity(AbstractStudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(abstractStudentEntity);
      assertEquals((Long) 1l, abstractStudentEntity.getId());
      assertEquals(Sex.MALE, abstractStudentEntity.getSex());
      assertEquals("190281-116Y", abstractStudentEntity.getSocialSecurityNumber());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindAbstractStudents() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/abstractStudents/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AbstractStudentEntity[] abstractStudentEntities = unserializeEntity(AbstractStudentEntity[].class, EntityUtils.toString(entity));
      assertNotNull(abstractStudentEntities);
      assertEquals(1, abstractStudentEntities.length);
      assertEquals((Long) 1l, abstractStudentEntities[0].getId());
      assertEquals(Sex.MALE, abstractStudentEntities[0].getSex());
      assertEquals("Test BasicInfo", abstractStudentEntities[0].getBasicInfo());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindAbstractStudentById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/abstractStudents/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AbstractStudentEntity abstractStudentEntity = unserializeEntity(AbstractStudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(abstractStudentEntity);
      assertEquals("190281-116Y", abstractStudentEntity.getSocialSecurityNumber());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateAbstractStudent() throws ClientProtocolException, IOException, ParseException {
    AbstractStudentEntity abstractStudentEntity = new AbstractStudentEntity();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date birthday = sdf.parse("1981-02-09");
    abstractStudentEntity.setBirthday(birthday);
    abstractStudentEntity.setBasicInfo("Updated BasicInfo");
    abstractStudentEntity.setSex(Sex.MALE);
    abstractStudentEntity.setSecureInfo(true);
    abstractStudentEntity.setSocialSecurityNumber("090281-116Y");
  
    HttpResponse response = doPutRequest("/students/abstractStudents/1", abstractStudentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      abstractStudentEntity = unserializeEntity(AbstractStudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(abstractStudentEntity);
      assertEquals(Sex.MALE, abstractStudentEntity.getSex());
      assertEquals("090281-116Y", abstractStudentEntity.getSocialSecurityNumber());
      assertEquals(true, abstractStudentEntity.getSecureInfo());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // StudentRESTServiceTest StudentStudyEndReason
  
  @Test
  public void testCreateStudentStudyEndReason() throws ClientProtocolException, IOException, ParseException {
    StudentStudyEndReasonEntity endReasonEntity = new StudentStudyEndReasonEntity();
    endReasonEntity.setName("Parent EndReason");
  
    HttpResponse response = doPostRequest("/students/endReasons/", endReasonEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      endReasonEntity = unserializeEntity(StudentStudyEndReasonEntity.class, EntityUtils.toString(entity));
      assertNotNull(endReasonEntity);
      assertEquals((Long) 1l, endReasonEntity.getId());
      assertEquals(null, endReasonEntity.getParentReason_id());
      assertEquals("Parent EndReason", endReasonEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testCreateStudentStudyEndReasonWithParent() throws ClientProtocolException, IOException, ParseException {
    StudentStudyEndReasonEntity endReasonEntity = new StudentStudyEndReasonEntity();
    endReasonEntity.setParentReason_id(1l);
    endReasonEntity.setName("Child EndReason");
  
    HttpResponse response = doPostRequest("/students/endReasons/", endReasonEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      endReasonEntity = unserializeEntity(StudentStudyEndReasonEntity.class, EntityUtils.toString(entity));
      assertNotNull(endReasonEntity);
      assertEquals((Long) 2l, endReasonEntity.getId());
      assertEquals((Long) 1l, endReasonEntity.getParentReason_id());
      assertEquals("Child EndReason", endReasonEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  

  @Test
  public void testFindStudentStudyEndReasons() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/endReasons/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentStudyEndReasonEntity[] endReasonEntities = unserializeEntity(StudentStudyEndReasonEntity[].class, EntityUtils.toString(entity));
      assertNotNull(endReasonEntities);
      assertEquals(2, endReasonEntities.length);
      assertEquals((Long) 1l, endReasonEntities[0].getId());
      assertEquals("Parent EndReason", endReasonEntities[0].getName());
      assertEquals((Long) 2l, endReasonEntities[1].getId());
      assertEquals("Child EndReason", endReasonEntities[1].getName());
      assertEquals((Long) 1l, endReasonEntities[1].getParentReason_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentStudyEndReasonById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/endReasons/2");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentStudyEndReasonEntity endReasonEntity = unserializeEntity(StudentStudyEndReasonEntity.class, EntityUtils.toString(entity));
      assertNotNull(endReasonEntity);
      assertEquals("Child EndReason", endReasonEntity.getName());
      assertEquals((Long) 1l, endReasonEntity.getParentReason_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudentStudyEndReason() throws ClientProtocolException, IOException, ParseException {
    StudentStudyEndReasonEntity endReasonEntity = new StudentStudyEndReasonEntity();
    endReasonEntity.setName("Updated Parent EndReason");
  
    HttpResponse response = doPutRequest("/students/endReasons/1", endReasonEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      endReasonEntity = unserializeEntity(StudentStudyEndReasonEntity.class, EntityUtils.toString(entity));
      assertNotNull(endReasonEntity);
      assertEquals("Updated Parent EndReason", endReasonEntity.getName());
      assertEquals(1, endReasonEntity.getChildEndReasons_ids().size());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // StudentRESTServiceTest StudyProgrammeCategory
  
  @Test
  public void testCreateStudyProgrammeCategory() throws ClientProtocolException, IOException, ParseException {
    StudyProgrammeCategoryEntity studyProgrammeCategoryEntity = new StudyProgrammeCategoryEntity();
    studyProgrammeCategoryEntity.setName("Test StudyProgrammeCategory");
    studyProgrammeCategoryEntity.setEducationType_id(1l);
  
    HttpResponse response = doPostRequest("/students/studyProgrammeCategories/", studyProgrammeCategoryEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studyProgrammeCategoryEntity = unserializeEntity(StudyProgrammeCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeCategoryEntity);
      assertEquals((Long) 1l, studyProgrammeCategoryEntity.getId());
      assertEquals((Long) 1l, studyProgrammeCategoryEntity.getEducationType_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudyProgrammeCategories() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studyProgrammeCategories/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudyProgrammeCategoryEntity[] studyProgrammeCategoryEntities = unserializeEntity(StudyProgrammeCategoryEntity[].class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeCategoryEntities);
      assertEquals(1, studyProgrammeCategoryEntities.length);
      assertEquals((Long) 1l, studyProgrammeCategoryEntities[0].getId());
      assertEquals("Test StudyProgrammeCategory", studyProgrammeCategoryEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudyProgrammeCategoryById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studyProgrammeCategories/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudyProgrammeCategoryEntity studyProgrammeCategoryEntity = unserializeEntity(StudyProgrammeCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeCategoryEntity);
      assertEquals("Test StudyProgrammeCategory", studyProgrammeCategoryEntity.getName());
      assertEquals((Long) 1l, studyProgrammeCategoryEntity.getEducationType_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudyProgrammeCategory() throws ClientProtocolException, IOException, ParseException {
    StudyProgrammeCategoryEntity studyProgrammeCategoryEntity = new StudyProgrammeCategoryEntity();
    studyProgrammeCategoryEntity.setName("Updated StudyProgrammeCategory");
    studyProgrammeCategoryEntity.setEducationType_id(1l);
  
    HttpResponse response = doPutRequest("/students/studyProgrammeCategories/1", studyProgrammeCategoryEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studyProgrammeCategoryEntity = unserializeEntity(StudyProgrammeCategoryEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeCategoryEntity);
      assertEquals("Updated StudyProgrammeCategory", studyProgrammeCategoryEntity.getName());
      assertEquals((Long) 1l, studyProgrammeCategoryEntity.getEducationType_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
 // StudentRESTServiceTest StudyProgramme
  
  @Test
  public void testCreateStudyProgramme() throws ClientProtocolException, IOException, ParseException {
    StudyProgrammeEntity studyProgrammeEntity = new StudyProgrammeEntity();
    studyProgrammeEntity.setName("Test StudyProgramme");
    studyProgrammeEntity.setCode("Test Code");
    studyProgrammeEntity.setCategory_id(1l);
  
    HttpResponse response = doPostRequest("/students/studyProgrammes/", studyProgrammeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studyProgrammeEntity = unserializeEntity(StudyProgrammeEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeEntity);
      assertEquals((Long) 1l, studyProgrammeEntity.getId());
      assertEquals("Test StudyProgramme", studyProgrammeEntity.getName());
      assertEquals("Test Code", studyProgrammeEntity.getCode());
      assertEquals((Long) 1l, studyProgrammeEntity.getCategory_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudyProgrammes() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studyProgrammes/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudyProgrammeEntity[] studyProgrammeEntities = unserializeEntity(StudyProgrammeEntity[].class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeEntities);
      assertEquals(1, studyProgrammeEntities.length);
      assertEquals((Long) 1l, studyProgrammeEntities[0].getId());
      assertEquals("Test StudyProgramme", studyProgrammeEntities[0].getName());
      assertEquals("Test Code", studyProgrammeEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudyProgrammeById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studyProgrammes/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudyProgrammeEntity studyProgrammeEntity = unserializeEntity(StudyProgrammeEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeEntity);
      assertEquals("Test StudyProgramme", studyProgrammeEntity.getName());
      assertEquals("Test Code", studyProgrammeEntity.getCode());
      assertEquals((Long) 1l, studyProgrammeEntity.getCategory_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudyProgramme() throws ClientProtocolException, IOException, ParseException {
    StudyProgrammeEntity studyProgrammeEntity = new StudyProgrammeEntity();
    studyProgrammeEntity.setName("Updated StudyProgramme");
    studyProgrammeEntity.setCode("Updated Code");
    studyProgrammeEntity.setCategory_id(1l);
  
    HttpResponse response = doPutRequest("/students/studyProgrammes/1", studyProgrammeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studyProgrammeEntity = unserializeEntity(StudyProgrammeEntity.class, EntityUtils.toString(entity));
      assertNotNull(studyProgrammeEntity);
      assertEquals("Updated StudyProgramme", studyProgrammeEntity.getName());
      assertEquals("Updated Code", studyProgrammeEntity.getCode());
      assertEquals((Long) 1l, studyProgrammeEntity.getCategory_id());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // StudentRESTServiceTest Municipality
  
  @Test
  public void testCreateMunicipality() throws ClientProtocolException, IOException, ParseException {
    MunicipalityEntity municipalityEntity = new MunicipalityEntity();
    municipalityEntity.setName("Test Municipality");
    municipalityEntity.setCode("Test MunicipalityCode");
  
    HttpResponse response = doPostRequest("/students/municipalities/", municipalityEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      municipalityEntity = unserializeEntity(MunicipalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(municipalityEntity);
      assertEquals((Long) 1l, municipalityEntity.getId());
      assertEquals("Test Municipality", municipalityEntity.getName());
      assertEquals("Test MunicipalityCode", municipalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindMunicipalities() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/municipalities/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      MunicipalityEntity[] municipalityEntities = unserializeEntity(MunicipalityEntity[].class, EntityUtils.toString(entity));
      assertNotNull(municipalityEntities);
      assertEquals(1, municipalityEntities.length);
      assertEquals((Long) 1l, municipalityEntities[0].getId());
      assertEquals("Test Municipality", municipalityEntities[0].getName());
      assertEquals("Test MunicipalityCode", municipalityEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindMunicipalityById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/municipalities/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      MunicipalityEntity municipalityEntity = unserializeEntity(MunicipalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(municipalityEntity);
      assertEquals("Test Municipality", municipalityEntity.getName());
      assertEquals("Test MunicipalityCode", municipalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateMunicipality() throws ClientProtocolException, IOException, ParseException {
    MunicipalityEntity municipalityEntity = new MunicipalityEntity();
    municipalityEntity.setName("Updated Municipality");
    municipalityEntity.setCode("Updated MunicipalityCode");
  
    HttpResponse response = doPutRequest("/students/municipalities/1", municipalityEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      municipalityEntity = unserializeEntity(MunicipalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(municipalityEntity);
      assertEquals("Updated Municipality", municipalityEntity.getName());
      assertEquals("Updated MunicipalityCode", municipalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
 // StudentRESTServiceTest Language
  
  @Test
  public void testCreateLanguage() throws ClientProtocolException, IOException, ParseException {
    LanguageEntity languageEntity = new LanguageEntity();
    languageEntity.setName("Test Language");
    languageEntity.setCode("Test LanguageCode");
  
    HttpResponse response = doPostRequest("/students/languages/", languageEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      languageEntity = unserializeEntity(LanguageEntity.class, EntityUtils.toString(entity));
      assertNotNull(languageEntity);
      assertEquals((Long) 1l, languageEntity.getId());
      assertEquals("Test Language", languageEntity.getName());
      assertEquals("Test LanguageCode", languageEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindLanguages() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/languages/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      LanguageEntity[] languageEntities = unserializeEntity(LanguageEntity[].class, EntityUtils.toString(entity));
      assertNotNull(languageEntities);
      assertEquals(1, languageEntities.length);
      assertEquals((Long) 1l, languageEntities[0].getId());
      assertEquals("Test Language", languageEntities[0].getName());
      assertEquals("Test LanguageCode", languageEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindLanguageById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/languages/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      LanguageEntity languageEntity = unserializeEntity(LanguageEntity.class, EntityUtils.toString(entity));
      assertNotNull(languageEntity);
      assertEquals("Test Language", languageEntity.getName());
      assertEquals("Test LanguageCode", languageEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateLanguage() throws ClientProtocolException, IOException, ParseException {
    LanguageEntity languageEntity = new LanguageEntity();
    languageEntity.setName("Updated Language");
    languageEntity.setCode("Updated LanguageCode");
  
    HttpResponse response = doPutRequest("/students/languages/1", languageEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      languageEntity = unserializeEntity(LanguageEntity.class, EntityUtils.toString(entity));
      assertNotNull(languageEntity);
      assertEquals("Updated Language", languageEntity.getName());
      assertEquals("Updated LanguageCode", languageEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // StudentRESTServiceTest Nationality

  @Test
  public void testCreateNationality() throws ClientProtocolException, IOException, ParseException {
    NationalityEntity nationalityEntity = new NationalityEntity();
    nationalityEntity.setName("Test Nationality");
    nationalityEntity.setCode("Test NationalityCode");

    HttpResponse response = doPostRequest("/students/nationalities/", nationalityEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      nationalityEntity = unserializeEntity(NationalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(nationalityEntity);
      assertEquals((Long) 1l, nationalityEntity.getId());
      assertEquals("Test Nationality", nationalityEntity.getName());
      assertEquals("Test NationalityCode", nationalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindNationalities() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/nationalities/");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      NationalityEntity[] nationalityEntities = unserializeEntity(NationalityEntity[].class, EntityUtils.toString(entity));
      assertNotNull(nationalityEntities);
      assertEquals(1, nationalityEntities.length);
      assertEquals((Long) 1l, nationalityEntities[0].getId());
      assertEquals("Test Nationality", nationalityEntities[0].getName());
      assertEquals("Test NationalityCode", nationalityEntities[0].getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindNationalityById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/nationalities/1");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      NationalityEntity nationalityEntity = unserializeEntity(NationalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(nationalityEntity);
      assertEquals("Test Nationality", nationalityEntity.getName());
      assertEquals("Test NationalityCode", nationalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateNationality() throws ClientProtocolException, IOException, ParseException {
    NationalityEntity nationalityEntity = new NationalityEntity();
    nationalityEntity.setName("Updated Nationality");
    nationalityEntity.setCode("Updated NationalityCode");

    HttpResponse response = doPutRequest("/students/nationalities/1", nationalityEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      nationalityEntity = unserializeEntity(NationalityEntity.class, EntityUtils.toString(entity));
      assertNotNull(nationalityEntity);
      assertEquals("Updated Nationality", nationalityEntity.getName());
      assertEquals("Updated NationalityCode", nationalityEntity.getCode());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //StudentRESTServiceTest StudentActivityType
  
  @Test
  public void testCreateStudentActivityType() throws ClientProtocolException, IOException, ParseException {
    StudentActivityTypeEntity activityTypeEntity = new StudentActivityTypeEntity();
    activityTypeEntity.setName("Test ActivityType");
  
    HttpResponse response = doPostRequest("/students/studentActivityTypes/", activityTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      activityTypeEntity = unserializeEntity(StudentActivityTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(activityTypeEntity);
      assertEquals((Long) 1l, activityTypeEntity.getId());
      assertEquals("Test ActivityType", activityTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentActivityTypes() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studentActivityTypes/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentActivityTypeEntity[] activityTypesEntities = unserializeEntity(StudentActivityTypeEntity[].class, EntityUtils.toString(entity));
      assertNotNull(activityTypesEntities);
      assertEquals(1, activityTypesEntities.length);
      assertEquals((Long) 1l, activityTypesEntities[0].getId());
      assertEquals("Test ActivityType", activityTypesEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentActivityTypeById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studentActivityTypes/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentActivityTypeEntity activityTypeEntity = unserializeEntity(StudentActivityTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(activityTypeEntity);
      assertEquals("Test ActivityType", activityTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudentActivityType() throws ClientProtocolException, IOException, ParseException {
    StudentActivityTypeEntity activityTypeEntity = new StudentActivityTypeEntity();
    activityTypeEntity.setName("Updated ActivityType");
  
    HttpResponse response = doPutRequest("/students/studentActivityTypes/1", activityTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      activityTypeEntity = unserializeEntity(StudentActivityTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(activityTypeEntity);
      assertEquals("Updated ActivityType", activityTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  //StudentRESTServiceTest StudentEducationalLevel
  
  @Test
  public void testCreateStudentEducationalLevel() throws ClientProtocolException, IOException, ParseException {
    StudentEducationalLevelEntity educationalLevelEntity = new StudentEducationalLevelEntity();
    educationalLevelEntity.setName("Test EducationalLevel");
  
    HttpResponse response = doPostRequest("/students/educationalLevels/", educationalLevelEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationalLevelEntity = unserializeEntity(StudentEducationalLevelEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalLevelEntity);
      assertEquals((Long) 1l, educationalLevelEntity.getId());
      assertEquals("Test EducationalLevel", educationalLevelEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentEducationalLevels() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/educationalLevels/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEducationalLevelEntity[] educationalLevelEntities = unserializeEntity(StudentEducationalLevelEntity[].class, EntityUtils.toString(entity));
      assertNotNull(educationalLevelEntities);
      assertEquals(1, educationalLevelEntities.length);
      assertEquals((Long) 1l, educationalLevelEntities[0].getId());
      assertEquals("Test EducationalLevel", educationalLevelEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentEducationalLevelById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/educationalLevels/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEducationalLevelEntity educationalLevelEntity = unserializeEntity(StudentEducationalLevelEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalLevelEntity);
      assertEquals("Test EducationalLevel", educationalLevelEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudentEducationalLevel() throws ClientProtocolException, IOException, ParseException {
    StudentEducationalLevelEntity educationalLevelEntity = new StudentEducationalLevelEntity();
    educationalLevelEntity.setName("Updated EducationalLevel");
  
    HttpResponse response = doPutRequest("/students/educationalLevels/1", educationalLevelEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      educationalLevelEntity = unserializeEntity(StudentEducationalLevelEntity.class, EntityUtils.toString(entity));
      assertNotNull(educationalLevelEntity);
      assertEquals("Updated EducationalLevel", educationalLevelEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
 //StudentRESTServiceTest StudentExaminationType
  
  @Test
  public void testCreateStudentExaminationType() throws ClientProtocolException, IOException, ParseException {
    StudentExaminationTypeEntity examinationTypeEntity = new StudentExaminationTypeEntity();
    examinationTypeEntity.setName("Test ExaminationType");
  
    HttpResponse response = doPostRequest("/students/examinationTypes/", examinationTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      examinationTypeEntity = unserializeEntity(StudentExaminationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(examinationTypeEntity);
      assertEquals((Long) 1l, examinationTypeEntity.getId());
      assertEquals("Test ExaminationType", examinationTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentExaminationTypes() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/examinationTypes/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentExaminationTypeEntity[] examinationTypeEntities = unserializeEntity(StudentExaminationTypeEntity[].class, EntityUtils.toString(entity));
      assertNotNull(examinationTypeEntities);
      assertEquals(1, examinationTypeEntities.length);
      assertEquals((Long) 1l, examinationTypeEntities[0].getId());
      assertEquals("Test ExaminationType", examinationTypeEntities[0].getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentExaminationTypeById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/examinationTypes/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentExaminationTypeEntity examinationTypeEntity = unserializeEntity(StudentExaminationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(examinationTypeEntity);
      assertEquals("Test ExaminationType", examinationTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudentExaminationType() throws ClientProtocolException, IOException, ParseException {
    StudentExaminationTypeEntity examinationTypeEntity = new StudentExaminationTypeEntity();
    examinationTypeEntity.setName("Updated ExaminationType");
  
    HttpResponse response = doPutRequest("/students/examinationTypes/1", examinationTypeEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      examinationTypeEntity = unserializeEntity(StudentExaminationTypeEntity.class, EntityUtils.toString(entity));
      assertNotNull(examinationTypeEntity);
      assertEquals("Updated ExaminationType", examinationTypeEntity.getName());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //StudentRESTServiceTest Student
  
  @Test
  public void testCreateStudent() throws ClientProtocolException, IOException, ParseException {
    StudentEntity studentEntity = new StudentEntity();
    studentEntity.setAbstractStudent_id(1l);
    studentEntity.setFirstName("Tero");
    studentEntity.setLastName("Testaaja");
    studentEntity.setNickname("Llama");
    studentEntity.setAdditionalInfo("Tero has four legs");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date studyTimeEnd = sdf.parse("2014-07-18");
    studentEntity.setStudyTimeEnd(studyTimeEnd);
    studentEntity.setActivityType_id(1l);
    studentEntity.setExaminationType_id(1l);
    studentEntity.setEducationalLevel_id(1l);
    studentEntity.setEducation("Test Education");
    studentEntity.setNationality_id(1l);
    studentEntity.setMunicipality_id(1l);
    studentEntity.setLanguage_id(1l);
    studentEntity.setSchool_id(1l);
    studentEntity.setStudyProgramme_id(1l);
    studentEntity.setPreviousStudies(20d);
    Date studyStartDate = sdf.parse("2013-09-09");
    studentEntity.setStudyStartDate(studyStartDate);
    Date studyEndDate = sdf.parse("2014-05-26");
    studentEntity.setStudyEndDate(studyEndDate);
    studentEntity.setStudyEndReason_id(1l);
    studentEntity.setStudyEndText("Test EndReason");
    studentEntity.setLodging(true);
  
    HttpResponse response = doPostRequest("/students/students/", studentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentEntity = unserializeEntity(StudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentEntity);
      assertEquals((Long) 1l, studentEntity.getId());
      assertEquals("Tero", studentEntity.getFirstName());
      assertEquals((Double) 20d, studentEntity.getPreviousStudies());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-09-09T00:00:00").getTime(), studentEntity.getStudyStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudents() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/students/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEntity[] studentEntities = unserializeEntity(StudentEntity[].class, EntityUtils.toString(entity));
      assertNotNull(studentEntities);
      assertEquals(1, studentEntities.length);
      assertEquals((Long) 1l, studentEntities[0].getId());
      assertEquals("Tero", studentEntities[0].getFirstName());
      assertEquals((Double) 20d, studentEntities[0].getPreviousStudies());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-09-09T00:00:00").getTime(), studentEntities[0].getStudyStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/students/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEntity studentEntity = unserializeEntity(StudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentEntity);
      assertEquals("Tero", studentEntity.getFirstName());
      assertEquals((Double) 20d, studentEntity.getPreviousStudies());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-09-09T00:00:00").getTime(), studentEntity.getStudyStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudent() throws ClientProtocolException, IOException, ParseException {
    StudentEntity studentEntity = new StudentEntity();
    studentEntity.setFirstName("Sakke");
    studentEntity.setLastName("Engineer");
    studentEntity.setNickname("The");
    studentEntity.setAdditionalInfo("The one and only");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date studyTimeEnd = sdf.parse("2020-01-01");
    studentEntity.setStudyTimeEnd(studyTimeEnd);
    studentEntity.setActivityType_id(1l);
    studentEntity.setExaminationType_id(1l);
    studentEntity.setEducationalLevel_id(1l);
    studentEntity.setEducation("Test Education");
    studentEntity.setNationality_id(1l);
    studentEntity.setMunicipality_id(1l);
    studentEntity.setLanguage_id(1l);
    studentEntity.setSchool_id(1l);
    studentEntity.setStudyProgramme_id(1l);
    studentEntity.setPreviousStudies(66.6d);
    Date studyStartDate = sdf.parse("2001-01-01");
    studentEntity.setStudyStartDate(studyStartDate);
    Date studyEndDate = sdf.parse("2019-12-24");
    studentEntity.setStudyEndDate(studyEndDate);
    studentEntity.setStudyEndReason_id(1l);
    studentEntity.setStudyEndText("Test EndReason");
    studentEntity.setLodging(true);
  
    HttpResponse response = doPutRequest("/students/students/1", studentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentEntity = unserializeEntity(StudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentEntity);
      assertEquals("Sakke", studentEntity.getFirstName());
      assertEquals("The one and only", studentEntity.getAdditionalInfo());
      assertEquals((Double) 66.6d, studentEntity.getPreviousStudies());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2001-01-01T00:00:00").getTime(), studentEntity.getStudyStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testArchiveStudent() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/students/students/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEntity studentEntity = unserializeEntity(StudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentEntity);
      assertEquals((Long) 1l, studentEntity.getId());
      assertEquals(true, studentEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveStudent() throws ClientProtocolException, IOException {
    StudentEntity studentEntity = new StudentEntity();
    studentEntity.setArchived(false);

    HttpResponse response = doPutRequest("/students/students/1", studentEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentEntity = unserializeEntity(StudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentEntity);
      assertEquals((Long) 1l, studentEntity.getId());
      assertEquals(false, studentEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testFindStudentsByAbstractStudent() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/abstractStudents/1/students");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentEntity[] studentEntities = unserializeEntity(StudentEntity[].class, EntityUtils.toString(entity));
      assertNotNull(studentEntities);
      assertEquals(1, studentEntities.length);
      assertEquals((Long) 1l, studentEntities[0].getId());
      assertEquals("Sakke", studentEntities[0].getFirstName());
      assertEquals("The one and only", studentEntities[0].getAdditionalInfo());
      assertEquals((Double) 66.6d, studentEntities[0].getPreviousStudies());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2001-01-01T00:00:00").getTime(), studentEntities[0].getStudyStartDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindAbstractStudentByStudent() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/students/1/abstractStudents");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      AbstractStudentEntity abstractStudentEntity = unserializeEntity(AbstractStudentEntity.class, EntityUtils.toString(entity));
      assertNotNull(abstractStudentEntity);
      assertEquals((Long) 1l, abstractStudentEntity.getId());
      assertEquals("090281-116Y", abstractStudentEntity.getSocialSecurityNumber());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  // StudentRESTServiceTest StudentTags
  
  @Test
  public void testCreateStudentTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("StudentTag");

    HttpResponse response = doPostRequest("/students/students/1/tags", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 5l, tagEntity.getId());
      assertEquals("StudentTag", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/students/1/tags");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(1, tagEntities.length);
      assertEquals("StudentTag", tagEntities[0].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testRemoveStudentTag() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/students/students/1/tags/5");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  }
  
 //StudentRESTServiceTest StudentGroup
  
  @Test
  public void testCreateStudentGroup() throws ClientProtocolException, IOException, ParseException {
    StudentGroupEntity studentGroupEntity = new StudentGroupEntity();
    studentGroupEntity.setName("Test Group");
    studentGroupEntity.setDescription("Group for rideable llamas");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = sdf.parse("2013-07-15");
    studentGroupEntity.setBeginDate(beginDate);
  
    HttpResponse response = doPostRequest("/students/studentGroups/", studentGroupEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentGroupEntity = unserializeEntity(StudentGroupEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntity);
      assertEquals((Long) 1l, studentGroupEntity.getId());
      assertEquals("Test Group", studentGroupEntity.getName());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-07-15T00:00:00").getTime(), studentGroupEntity.getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentGroups() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studentGroups/");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentGroupEntity[] studentGroupEntities = unserializeEntity(StudentGroupEntity[].class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntities);
      assertEquals(1, studentGroupEntities.length);
      assertEquals((Long) 1l, studentGroupEntities[0].getId());
      assertEquals("Test Group", studentGroupEntities[0].getName());
      assertEquals("Group for rideable llamas", studentGroupEntities[0].getDescription());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-07-15T00:00:00").getTime(), studentGroupEntities[0].getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentGroupById() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studentGroups/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentGroupEntity studentGroupEntity = unserializeEntity(StudentGroupEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntity);
      assertEquals("Test Group", studentGroupEntity.getName());
      assertEquals("Group for rideable llamas", studentGroupEntity.getDescription());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-07-15T00:00:00").getTime(), studentGroupEntity.getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testUpdateStudentGroup() throws ClientProtocolException, IOException, ParseException {
    StudentGroupEntity studentGroupEntity = new StudentGroupEntity();
    studentGroupEntity.setName("Updated Group");
    studentGroupEntity.setDescription("Group for engineers");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = sdf.parse("2013-09-13");
    studentGroupEntity.setBeginDate(beginDate);
    
    HttpResponse response = doPutRequest("/students/studentGroups/1", studentGroupEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentGroupEntity = unserializeEntity(StudentGroupEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntity);
      assertEquals("Updated Group", studentGroupEntity.getName());
      assertEquals("Group for engineers", studentGroupEntity.getDescription());
      assertEquals(javax.xml.bind.DatatypeConverter.parseDateTime("2013-09-13T00:00:00").getTime(), studentGroupEntity.getBeginDate());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testArchiveStudentGroup() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/students/studentGroups/1");
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      StudentGroupEntity studentGroupEntity = unserializeEntity(StudentGroupEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntity);
      assertEquals((Long) 1l, studentGroupEntity.getId());
      assertEquals(true, studentGroupEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  @Test
  public void testUnarchiveStudentGroup() throws ClientProtocolException, IOException {
    StudentGroupEntity studentGroupEntity = new StudentGroupEntity();
    studentGroupEntity.setArchived(false);

    HttpResponse response = doPutRequest("/students/studentGroups/1", studentGroupEntity);
  
    assertEquals(200, response.getStatusLine().getStatusCode());
  
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      studentGroupEntity = unserializeEntity(StudentGroupEntity.class, EntityUtils.toString(entity));
      assertNotNull(studentGroupEntity);
      assertEquals((Long) 1l, studentGroupEntity.getId());
      assertEquals(false, studentGroupEntity.getArchived());
    } finally {
      EntityUtils.consume(entity);
    }
  }
  
  //StudentRESTService StudentGroupTags
  
  @Test
  public void testCreateStudentGroupTag() throws ClientProtocolException, IOException {
    TagEntity tagEntity = new TagEntity();
    tagEntity.setText("StudentGroupTag");

    HttpResponse response = doPostRequest("/students/studentGroups/1/tags", tagEntity);

    assertEquals(200, response.getStatusLine().getStatusCode());
    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      tagEntity = unserializeEntity(TagEntity.class, EntityUtils.toString(entity));
      assertNotNull(tagEntity);
      assertEquals((Long) 6l, tagEntity.getId());
      assertEquals("StudentGroupTag", tagEntity.getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testFindStudentGroupTags() throws ClientProtocolException, IOException {
    HttpResponse response = doGetRequest("/students/studentGroups/1/tags");

    assertEquals(200, response.getStatusLine().getStatusCode());

    HttpEntity entity = response.getEntity();
    try {
      assertNotNull(entity);
      assertEquals("application/json", entity.getContentType().getValue());
      TagEntity[] tagEntities = unserializeEntity(TagEntity[].class, EntityUtils.toString(entity));
      assertNotNull(tagEntities);
      assertEquals(1, tagEntities.length);
      assertEquals("StudentGroupTag", tagEntities[0].getText());
    } finally {
      EntityUtils.consume(entity);
    }
  }

  @Test
  public void testRemoveStudentGroupTag() throws ClientProtocolException, IOException {
    HttpResponse response = doDeleteRequest("/students/studentGroups/1/tags/6");

    assertEquals(200, response.getStatusLine().getStatusCode());
  }
}
