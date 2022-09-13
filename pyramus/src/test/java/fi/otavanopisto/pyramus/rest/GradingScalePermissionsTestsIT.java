package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.GradingScale;

@RunWith(Parameterized.class)
public class GradingScalePermissionsTestsIT extends AbstractRESTPermissionsTest {

  private CommonPermissions commonPermissions = new CommonPermissions();

  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }

  public GradingScalePermissionsTestsIT(String role) {
    this.role = role;
  }

  @Test
  public void testPermissionsCreateGradingScale() throws NoSuchFieldException {
    GradingScale gradingScale = new GradingScale(null, "create scale", "grading scale for testing creation", Boolean.FALSE);
    Response response;

    response = given().headers(getAuthHeaders())
        .contentType("application/json").body(gradingScale)
        .post("/common/gradingScales");

    assertOk(response, commonPermissions, CommonPermissions.CREATE_GRADINGSCALE, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders()).delete(
            "/common/gradingScales/{ID}?permanent=true", id);
      }
    }
  }

  @Test
  public void testPermissionsListGradingScales() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders()).get(
        "/common/gradingScales");
    assertOk(response, commonPermissions, CommonPermissions.LIST_GRADINGSCALES,
        200);
  }

  @Test
  public void testPermissionsFindGradingScale() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders()).get(
        "/common/gradingScales/{ID}", 1);
    assertOk(response, commonPermissions, CommonPermissions.FIND_GRADINGSCALE,
        200);
  }

  @Test
  public void testPermissionsUpdateGradingScale() throws NoSuchFieldException {
    GradingScale gradingScale = new GradingScale(null, "not updated",
        "grading scale has not been updated yet", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
        .contentType("application/json").body(gradingScale)
        .post("/common/gradingScales");

    response.then().body("id", not(is((Long) null)))
        .body("name", is(gradingScale.getName()))
        .body("description", is(gradingScale.getDescription()))
        .body("archived", is(gradingScale.getArchived()));

    Long id = response.body().jsonPath().getLong("id");
    assertNotNull(id);

    try {
      GradingScale updateScale = new GradingScale(id, "updated",
          "grading scale has been updated", Boolean.FALSE);

      Response updateResponse = given().headers(getAuthHeaders())
          .contentType("application/json").body(updateScale)
          .put("/common/gradingScales/{ID}", id);

      assertOk(updateResponse, commonPermissions,
          CommonPermissions.UPDATE_GRADINGSCALE, 200);

    } finally {
      given().headers(getAdminAuthHeaders()).delete(
          "/common/gradingScales/{ID}?permanent=true", id);
    }
  }

  @Test
  public void testPermissionsDeleteGradingScale() throws NoSuchFieldException {
    GradingScale gradingScale = new GradingScale(null, "to be deleted", "grading scale to be deleted", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(gradingScale)
      .post("/common/gradingScales");

    Long id = response.body().jsonPath().getLong("id");

    try {
      Response deleteResponse = given().headers(getAuthHeaders())
          .delete("/common/gradingScales/{ID}", id);
      assertOk(deleteResponse, commonPermissions, CommonPermissions.DELETE_GRADINGSCALE, 204);
    } finally {
      given().headers(getAdminAuthHeaders()).delete(
          "/common/gradingScales/{ID}?permanent=true", id);
    }  
  }
}
