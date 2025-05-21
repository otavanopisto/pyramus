package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.PersonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.StudentPermissions;
import fi.otavanopisto.pyramus.rest.model.Person;
import fi.otavanopisto.pyramus.rest.model.Sex;
import io.restassured.response.Response;

public class PersonPermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {
  
  // TODO: tests for default person

  private PersonPermissions personPermissions = new PersonPermissions();
  private StudentPermissions studentPermissions = new StudentPermissions();

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreatePerson(Role role) throws NoSuchFieldException {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be created", null);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    assertOk(role, response, personPermissions, PersonPermissions.CREATE_PERSON);

    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/persons/persons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListPersons(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/persons/persons");
    
    assertOk(role, response, personPermissions, PersonPermissions.LIST_PERSONS);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindPerson(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/persons/persons/{ID}", 3);
    
    assertOk(role, response, personPermissions, PersonPermissions.FIND_PERSON);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindPersonOppija(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/persons/persons/{ID}/oppija", 3);

    int expectedStatusCode = role == Role.ADMINISTRATOR ? 200 : role == Role.EVERYONE ? 403 : 404;
    int statusCode = response.statusCode();
    
    assertEquals(String.format("Status code <%d> didn't match expected code <%d> when Role = %s", statusCode, expectedStatusCode, role),
        expectedStatusCode, statusCode);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdatePerson(Role role) throws NoSuchFieldException {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "not updated", null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Person updateStudent = new Person(id, getDate(1991, 7, 7), "1234567-9876", Sex.MALE, true, "updated", null);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateStudent)
        .put("/persons/persons/{ID}", id);

      assertOk(role, response, personPermissions, PersonPermissions.UPDATE_PERSON);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/persons/persons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeletePerson(Role role) throws NoSuchFieldException {
    Person person = new Person(null, getDate(1990, 6, 6), "1234567-0987", Sex.FEMALE, false, "to be deleted", null);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(person)
      .post("/persons/persons");

    int id = response.body().jsonPath().getInt("id");

    response = given().headers(getAuthHeaders(role))
      .delete("/persons/persons/{ID}", id);
    
    assertOk(role, response, personPermissions, PersonPermissions.DELETE_PERSON, 204);
    
    if (response.getStatusCode() != 204) {
      given().headers(getAdminAuthHeaders())
        .delete("/persons/persons/{ID}", id)
        .then()
        .statusCode(204);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListStudents(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))   
      .get("/persons/persons/{ID}/students", 3l);
    
    assertOk(role, response, studentPermissions, StudentPermissions.LIST_STUDENTSBYPERSON);
  }
}
