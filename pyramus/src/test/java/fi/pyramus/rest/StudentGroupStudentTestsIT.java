package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import com.jayway.restassured.response.Response;

public class StudentGroupStudentTestsIT extends AbstractRESTServiceTest {

  @Test
  public void testCreateStudentGroupStudent() {
    given()
      .get("/students/studentGroups/{ID}/students", 2)
      .then()
      .statusCode(204);
    
    fi.pyramus.rest.model.StudentGroupStudent entity = new fi.pyramus.rest.model.StudentGroupStudent(null, 1l);
    
    Response response = given()
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("studentId", is(entity.getStudentId().intValue()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    try {
      given()
        .get("/students/studentGroups/{ID}/students", 2)
        .then()
        .statusCode(200)
        .body("id.size()", is(1))
        .body("id[0]", is(id.intValue()) )
        .body("studentId[0]", is(1));
    } finally {
      given()
        .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testListStudentGroupStudents() {
    given()
      .get("/students/studentGroups/{ID}/students", 1)
      .then()
      .statusCode(200)
      .body("id.size()", is(2))
      .body("id[0]", is(1) )
      .body("studentId[0]", is(1))
      .body("id[1]", is(2) )
      .body("studentId[1]", is(2));
  }
  
  @Test
  public void testFindStudentGroupStudent() {
    given()
      .get("/students/studentGroups/{GROUPID}/students/{ID}", 1l, 1l)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("studentId", is(1));
  }
  
  @Test
  public void testDeleteStudentGroupStudent() {
    fi.pyramus.rest.model.StudentGroupStudent entity = new fi.pyramus.rest.model.StudentGroupStudent(null, 1l);
    
    Response response = given()
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups/{ID}/students", 2l);

    response.then()
      .body("id", not(is((Long) null)))
      .body("studentId", is(entity.getStudentId().intValue()));
      
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    given()
      .get("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id)
      .then()
      .statusCode(200);
    
    given()
      .delete("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id)
      .then()
      .statusCode(204);
    
    given()
      .get("/students/studentGroups/{GROUPID}/students/{ID}", 2l, id)
      .then()
      .statusCode(404);
  }
  
  
}
