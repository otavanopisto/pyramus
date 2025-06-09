package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.Grade;
import io.restassured.response.Response;

public class GradePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateGrade(Role role) throws NoSuchFieldException {
    Grade grade = new Grade(null, "create grade", "grade for testing grading creation", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());
    
    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_GRADE, 200);
    
    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders())
          .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListGrades(Role role) throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/gradingScales/1/grades");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_GRADES, 200);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindGradingScale(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/common/gradingScales/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_GRADINGSCALE, 200);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateGrade(Role role) throws NoSuchFieldException {
    Grade grade = new Grade(null, "not updated", "not updated grade", 1l, Boolean.FALSE, "not updated qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());;
    
    Long id = response.body().jsonPath().getLong("id");
    
    try {
      Grade updateGrade = new Grade(id, "updated", "updated grade", 1l, Boolean.TRUE, "updated qualification", 10d, Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateGrade)
        .put("/common/gradingScales/{SCALEID}/grades/{ID}", updateGrade.getGradingScaleId(), id);
      assertOk(role, updateResponse, commonPermissions, CommonPermissions.UPDATE_GRADE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteGrade(Role role) throws NoSuchFieldException {
    Grade grade = new Grade(null, "to be deleted", "grade to be deleted", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());

    Long id = response.body().jsonPath().getLong("id");
    
    Response deleteResponse = given().headers(getAuthHeaders(role))
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id);
    assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_GRADE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
  }
}
