package fi.otavanopisto.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.jayway.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.Project;

public class ProjectTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateProject() {
    Project project = new Project(null, 
        "to be created", 
        "project to be created", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(project.getName()))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("optionalStudiesLength", is(project.getOptionalStudiesLength().floatValue() ))
      .body("optionalStudiesLengthUnitId", is(project.getOptionalStudiesLengthUnitId().intValue() ))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( project.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/projects/projects/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListProjects() {
    given().headers(getAuthHeaders())
      .get("/projects/projects")
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("name[0]", is("Test Project #1"))
      .body("description[0]", is("Project for testing"))
      .body("created[0]", is(getDate(2010, 6, 6).toString()))
      .body("lastModified[0]", is(getDate(2010, 6, 6).toString()))
      .body("creatorId[0]", is(1))
      .body("lastModifierId[0]", is(1))
      .body("optionalStudiesLength[0]", is(678f))
      .body("optionalStudiesLengthUnitId[0]", is(1))
      .body("id[1]", is(2) )
      .body("name[1]", is("Test Project #2"))
      .body("description[1]", is("Project for testing"))
      .body("created[1]", is(getDate(2011, 6, 6).toString()))
      .body("lastModified[1]", is(getDate(2011, 6, 6).toString()))
      .body("creatorId[1]", is(1))
      .body("lastModifierId[1]", is(1))
      .body("optionalStudiesLength[1]", is(910f))
      .body("optionalStudiesLengthUnitId[1]", is(1));
  }
  
  @Test
  public void testFindProject() {
    given().headers(getAuthHeaders())
      .get("/projects/projects/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("Test Project #1"))
      .body("description", is("Project for testing"))
      .body("created", is(getDate(2010, 6, 6).toString()))
      .body("lastModified", is(getDate(2010, 6, 6).toString()))
      .body("creatorId", is(1))
      .body("lastModifierId", is(1))
      .body("optionalStudiesLength", is(678f))
      .body("optionalStudiesLengthUnitId", is(1));
  }
  
  @Test
  public void testUpdateProjects() {
    Project project = new Project(null, 
        "not updated", 
        "not updated project", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(project.getName()))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("optionalStudiesLength", is(project.getOptionalStudiesLength().floatValue() ))
      .body("optionalStudiesLengthUnitId", is(project.getOptionalStudiesLengthUnitId().intValue() ))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( project.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      Project updateProject = new Project(id, 
        "updated", 
        "updated project", 
        234d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag2", "tag3"), 
        Boolean.FALSE);

      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateProject)
        .put("/projects/projects/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateProject.getId().intValue() ))
        .body("name", is(updateProject.getName()))
        .body("creatorId", not(is((Long) null)))
        .body("lastModifierId", not(is((Long) null)))
        .body("created", not(is((String) null)))
        .body("lastModified", not(is((String) null)))
        .body("optionalStudiesLength", is(updateProject.getOptionalStudiesLength().floatValue() ))
        .body("optionalStudiesLengthUnitId", is(updateProject.getOptionalStudiesLengthUnitId().intValue() ))
        .body("tags.size()", is(2))
        .body("tags", allOf(hasItem("tag2"), hasItem("tag3") ))
        .body("archived", is( updateProject.getArchived() ));

    } finally {
      given().headers(getAuthHeaders())
        .delete("/projects/projects/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteProject() {
    Project project = new Project(null, 
        "to be deleted", 
        "to be deleted", 
        123d, 
        1l, 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(project)
      .post("/projects/projects");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(project.getName()))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("optionalStudiesLength", is(project.getOptionalStudiesLength().floatValue() ))
      .body("optionalStudiesLengthUnitId", is(project.getOptionalStudiesLengthUnitId().intValue() ))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( project.getArchived() ));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/projects/projects/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/projects/projects/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/projects/projects/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/projects/projects/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/projects/projects/{ID}", id)
      .then()
      .statusCode(404);
  }
}
