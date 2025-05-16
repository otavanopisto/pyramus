package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.model.StudentGroup;

public class StudentGroupTestsIT extends AbstractRESTServiceTest {

  private static final Long TEST_ORGANIZATION_ID = 1l;

  @Test
  public void testCreateStudentGroup() {
    StudentGroup entity = new StudentGroup(
        null, 
        "to be created", 
        "student group to be created", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()))
      .body("description", is(entity.getDescription()))
      .body("beginDate", is(getDateString(entity.getBeginDate().getYear(), entity.getBeginDate().getMonth().getValue(), entity.getBeginDate().getDayOfMonth()) ))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( entity.getArchived() ));
      
    int id = response.body().jsonPath().getInt("id");
    
    given().headers(getAuthHeaders())
      .delete("/students/studentGroups/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
  }
  
  @Test
  public void testListStudentGroups() {
    // TODO: StudentGroupStudentTestsIT.testCreateStudentGroupStudent 
    // changes the lastModifier so we can not check whether its the correct one or not
    
    given().headers(getAuthHeaders())
      .get("/students/studentGroups")
      .then()
      .statusCode(200)
      .body("id.size()", is(3))
      .body("id[0]", is(1) )
      .body("name[0]", is("StudentGroup #1"))
      .body("description[0]", is("Group of students #1"))
      .body("beginDate[0]", is(getDateString(2010, 1, 1) ))
      .body("creatorId[0]", is(1))
      .body("lastModifierId[0]", not(is((Long) null)))
//      .body("lastModifierId[0]", is(1))
      .body("created[0]", is(getDateString(2010, 2, 2) ))
      .body("lastModified[0]", not(is((String) null)))
      .body("archived[0]", is( Boolean.FALSE ))
      .body("id[1]", is(2) )
      .body("name[1]", is("StudentGroup #2"))
      .body("description[1]", is("Group of students #2"))
      .body("beginDate[1]", is(getDateString(2010, 4, 4)))
      .body("creatorId[1]", is(1))
      .body("lastModifierId[1]", not(is((Long) null)))
//      .body("lastModifierId[1]", is(1))
      .body("created[1]", is(getDateString(2010, 5, 5) ))
      .body("lastModified[1]", not(is((String) null)))
      .body("archived[1]", is( Boolean.FALSE ));
  }
  
  @Test
  public void testFindStudentGroup() {
    given().headers(getAuthHeaders())
      .get("/students/studentGroups/{ID}", 1)
      .then()
      .statusCode(200)
      .body("id", is(1) )
      .body("name", is("StudentGroup #1"))
      .body("description", is("Group of students #1"))
      .body("beginDate", is(getDateString(2010, 1, 1) ))
      .body("creatorId", is(1))
      .body("lastModifierId", is(1))
      .body("created", is(getDateString(2010, 2, 2) ))
      .body("lastModified", is(getDateString(2010, 3, 3) ))
      .body("archived", is( Boolean.FALSE ));
  }
  
  @Test
  public void testUpdateStudentGroup() {
    StudentGroup entity = new StudentGroup(null, 
        "not updated", 
        "not updated student group", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()))
      .body("description", is(entity.getDescription()))
      .body("beginDate", is(getDateString(entity.getBeginDate().getYear(), entity.getBeginDate().getMonth().getValue(), entity.getBeginDate().getDayOfMonth()) ))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( entity.getArchived()));
      
    Long id = response.body().jsonPath().getLong("id");
    try {
      StudentGroup updateEntity = new StudentGroup(id, 
          "updated", 
          "updated student group", 
          getDate(2015, 7, 7), 
          null, 
          null, 
          null, 
          null, 
          Arrays.asList("tag2", "tag3"), 
          Boolean.FALSE,
          TEST_ORGANIZATION_ID, // organizationId
          Boolean.FALSE);
      
      given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateEntity)
        .put("/students/studentGroups/{ID}", id)
        .then()
        .statusCode(200)
        .body("id", is(updateEntity.getId().intValue() ))
        .body("name", is(updateEntity.getName()))
        .body("description", is(updateEntity.getDescription()))
        .body("beginDate", is(getDateString(updateEntity.getBeginDate().getYear(), updateEntity.getBeginDate().getMonth().getValue(), updateEntity.getBeginDate().getDayOfMonth()) ))
        .body("creatorId", not(is((Long) null)))
        .body("lastModifierId", not(is((Long) null)))
        .body("created", not(is((String) null)))
        .body("lastModified", not(is((String) null)))
        .body("tags.size()", is(2))
        .body("tags", allOf(hasItem("tag2"), hasItem("tag3") ))
        .body("archived", is( updateEntity.getArchived()));
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", id)
        .then()
        .statusCode(204);
    }
  }
  
  @Test
  public void testDeleteStudentGroup() {
    StudentGroup entity = new StudentGroup(null, 
        "to be created", 
        "student group to be created", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.FALSE,
        TEST_ORGANIZATION_ID, // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    response.then()
      .body("id", not(is((Long) null)))
      .body("name", is(entity.getName()))
      .body("description", is(entity.getDescription()))
      .body("creatorId", not(is((Long) null)))
      .body("lastModifierId", not(is((Long) null)))
      .body("created", not(is((String) null)))
      .body("lastModified", not(is((String) null)))
      .body("tags.size()", is(2))
      .body("tags", allOf(hasItem("tag1"), hasItem("tag2") ))
      .body("archived", is( entity.getArchived() ));
      
    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);
    
    given().headers(getAuthHeaders()).get("/students/studentGroups/{ID}", id)
      .then()
      .statusCode(200);
    
    given().headers(getAuthHeaders())
      .delete("/students/studentGroups/{ID}", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studentGroups/{ID}", id)
      .then()
      .statusCode(404);
    
    given().headers(getAuthHeaders())
      .delete("/students/studentGroups/{ID}?permanent=true", id)
      .then()
      .statusCode(204);
    
    given().headers(getAuthHeaders()).get("/students/studentGroups/{ID}", id)
      .then()
      .statusCode(404);
  }

  @Test
  public void testListStudentGroupStudentGuidanceCounselors() {
    final Long COUNCELOR_STUDENT_ID = 3l;
    final Long COUNCELOR_STAFF_ID = 10l;
    
    StudentGroup entity = new StudentGroup(null, 
        "temp guidance councelor test group", 
        "temp guidance councelor test group", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.TRUE,                           // guidanceGroup
        TEST_ORGANIZATION_ID,                   // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    response.then()
      .body("id", not(is((Long) null)));

    Long studentGroupId = response.body().jsonPath().getLong("id");
    given().headers(getAuthHeaders()).get("/students/studentGroups/{ID}", studentGroupId)
      .then()
      .statusCode(200);
    try {
      fi.otavanopisto.pyramus.rest.model.StudentGroupStudent studentGroupStudent = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, COUNCELOR_STUDENT_ID);
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(studentGroupStudent)
        .post("/students/studentGroups/{ID}/students", studentGroupId);
      Long studentGroupStudentId = response.body().jsonPath().getLong("id");

      try {
        fi.otavanopisto.pyramus.rest.model.StudentGroupUser studentGroupUser = new fi.otavanopisto.pyramus.rest.model.StudentGroupUser(null, COUNCELOR_STAFF_ID, false, false, false);
        response = given().headers(getAuthHeaders())
            .contentType("application/json")
            .body(studentGroupUser)
            .post("/students/studentGroups/{ID}/staffmembers", studentGroupId);
        Long studentGroupUserId = response.body().jsonPath().getLong("id");
        try {
          // Actual test
          given().headers(getAuthHeaders())
            .get("/students/students/{ID}/guidanceCounselors", COUNCELOR_STUDENT_ID)
            .then()
            .statusCode(200)
            .body("id.size()", is(1))
            .body("id[0]", is(studentGroupUserId.intValue()))
            .body("staffMemberId[0]", is(COUNCELOR_STAFF_ID.intValue()));
          
          // Test that there's no message recipients
          given().headers(getAuthHeaders())
            .get("/students/students/{ID}/guidanceCounselors?onlyMessageRecipients=true", COUNCELOR_STUDENT_ID)
            .then()
            .statusCode(200)
            .body("$.size()", is(0));
          
        } finally {
          given().headers(getAuthHeaders())
            .contentType("application/json")
            .delete("/students/studentGroups/{ID}/staffmembers/{ID2}", studentGroupId, studentGroupUserId)
            .then()
            .statusCode(204);
        }
      } finally {
        given().headers(getAuthHeaders())
          .contentType("application/json")
          .delete("/students/studentGroups/{ID}/students/{ID2}", studentGroupId, studentGroupStudentId)
          .then()
          .statusCode(204);
      }
      
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", studentGroupId)
        .then()
        .statusCode(204);
    }
    
  }
  
  @Test
  public void testListStudentGroupStudentGuidanceCounselors2() {
    final Long COUNCELOR_STUDENT_ID = 3l;
    final Long COUNCELOR_STAFF_ID = 10l;
    final Long COUNCELOR_STAFF_ID2 = 11l;
    
    StudentGroup entity = new StudentGroup(null, 
        "temp guidance councelor test group", 
        "temp guidance councelor test group", 
        getDate(2014, 6, 6), 
        null, 
        null, 
        null, 
        null, 
        Arrays.asList("tag1", "tag2"), 
        Boolean.TRUE,                           // guidanceGroup
        TEST_ORGANIZATION_ID,                   // organizationId
        Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(entity)
      .post("/students/studentGroups");

    response.then()
      .body("id", not(is((Long) null)));

    Long studentGroupId = response.body().jsonPath().getLong("id");
    given().headers(getAuthHeaders()).get("/students/studentGroups/{ID}", studentGroupId)
      .then()
      .statusCode(200);
    try {
      fi.otavanopisto.pyramus.rest.model.StudentGroupStudent studentGroupStudent = new fi.otavanopisto.pyramus.rest.model.StudentGroupStudent(null, COUNCELOR_STUDENT_ID);
      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(studentGroupStudent)
        .post("/students/studentGroups/{ID}/students", studentGroupId);
      Long studentGroupStudentId = response.body().jsonPath().getLong("id");

      try {
        fi.otavanopisto.pyramus.rest.model.StudentGroupUser studentGroupUser = new fi.otavanopisto.pyramus.rest.model.StudentGroupUser(null, COUNCELOR_STAFF_ID, false, false, false);
        response = given().headers(getAuthHeaders())
            .contentType("application/json")
            .body(studentGroupUser)
            .post("/students/studentGroups/{ID}/staffmembers", studentGroupId);
        Long studentGroupUserId = response.body().jsonPath().getLong("id");
        try {
          
          // Message recipient test
          
          
          studentGroupUser = new fi.otavanopisto.pyramus.rest.model.StudentGroupUser(null, COUNCELOR_STAFF_ID2, false, false, false);
          response = given().headers(getAuthHeaders())
              .contentType("application/json")
              .body(studentGroupUser)
              .post("/students/studentGroups/{ID}/staffmembers", studentGroupId);
          Long studentGroupUserId2 = response.body().jsonPath().getLong("id");
          try {

            // Actual test
            given().headers(getAuthHeaders())
              .get("/students/students/{ID}/guidanceCounselors", COUNCELOR_STUDENT_ID)
              .then()
              .statusCode(200)
              .body("id.size()", is(2))
              .body("id[0]", is(studentGroupUserId.intValue()))
              .body("staffMemberId[0]", is(COUNCELOR_STAFF_ID.intValue()));

            // Test that there's no message recipients
            given().headers(getAuthHeaders())
              .get("/students/students/{ID}/guidanceCounselors?onlyMessageRecipients=true", COUNCELOR_STUDENT_ID)
              .then()
              .statusCode(200)
              .body("$.size()", is(0));
            
          } finally {
            given().headers(getAuthHeaders())
              .contentType("application/json")
              .delete("/students/studentGroups/{ID}/staffmembers/{ID2}", studentGroupId, studentGroupUserId2)
              .then()
              .statusCode(204);
          }
        } finally {
          given().headers(getAuthHeaders())
            .contentType("application/json")
            .delete("/students/studentGroups/{ID}/staffmembers/{ID2}", studentGroupId, studentGroupUserId)
            .then()
            .statusCode(204);
        }
      } finally {
        given().headers(getAuthHeaders())
          .contentType("application/json")
          .delete("/students/studentGroups/{ID}/students/{ID2}", studentGroupId, studentGroupStudentId)
          .then()
          .statusCode(204);
      }
      
    } finally {
      given().headers(getAuthHeaders())
        .delete("/students/studentGroups/{ID}?permanent=true", studentGroupId)
        .then()
        .statusCode(204);
    }
    
  }
  
}
