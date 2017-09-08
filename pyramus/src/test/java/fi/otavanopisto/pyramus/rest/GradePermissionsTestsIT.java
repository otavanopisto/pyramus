package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.Grade;

@RunWith(Parameterized.class)
public class GradePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();
  
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  public GradePermissionsTestsIT(String role) {
    this.role = role;
  }
  
  @Test
  public void testPermissionsCreateGrade() throws NoSuchFieldException {
    Grade grade = new Grade(null, "create grade", "grade for testing grading creation", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());
    
    assertOk(response, commonPermissions, CommonPermissions.CREATE_GRADE, 200);
    
    Long statusCode = new Long(response.statusCode());
    Long id;
    if(statusCode.toString().equals("200")){
      id = new Long(response.body().jsonPath().getInt("id"));
      if (!id.equals(null)) {
        given().headers(getAdminAuthHeaders())
        .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
      }
    }
  }

  @Test
  public void testPermissionsListGrades() throws NoSuchFieldException{
    Response response = given().headers(getAuthHeaders())
      .get("/common/gradingScales/1/grades");
    assertOk(response, commonPermissions, CommonPermissions.LIST_GRADES, 200);
  }

  @Test
  public void testPermissionsFindGradingScale() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/common/gradingScales/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_GRADINGSCALE, 200);
  }
  
  @Test
  public void testPermissionsUpdateGrade() throws NoSuchFieldException {
    Grade grade = new Grade(null, "not updated", "not updated grade", 1l, Boolean.FALSE, "not updated qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());;
    
    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    try {
      Grade updateGrade = new Grade(id, "updated", "updated grade", 1l, Boolean.TRUE, "updated qualification", 10d, Boolean.FALSE);
      
      Response updateResponse = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateGrade)
        .put("/common/gradingScales/{SCALEID}/grades/{ID}", updateGrade.getGradingScaleId(), id);
      assertOk(updateResponse, commonPermissions, CommonPermissions.UPDATE_GRADE, 200);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
    }
  }
  
  @Test
  public void testPermissionsDeleteGrade() throws NoSuchFieldException {
    Grade grade = new Grade(null, "to be deleted", "grade to be deleted", 1l, Boolean.TRUE, "qualification", 5d, Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(grade)
      .post("/common/gradingScales/{SCALEID}/grades", grade.getGradingScaleId());

    Long id = new Long(response.body().jsonPath().getInt("id"));
    
    Response deleteResponse = given().headers(getAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}", grade.getGradingScaleId(), id);
    assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_GRADE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/common/gradingScales/{SCALEID}/grades/{ID}?permanent=true", grade.getGradingScaleId(), id);
  }
}
