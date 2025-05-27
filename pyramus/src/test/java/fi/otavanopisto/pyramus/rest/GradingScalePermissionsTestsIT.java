package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.model.GradingScale;
import io.restassured.response.Response;

public class GradingScalePermissionsTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private CommonPermissions commonPermissions = new CommonPermissions();

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsCreateGradingScale(Role role) throws NoSuchFieldException {
    GradingScale gradingScale = new GradingScale(null, "create scale", "grading scale for testing creation", Boolean.FALSE);
    Response response;

    response = given().headers(getAuthHeaders(role))
        .contentType("application/json").body(gradingScale)
        .post("/common/gradingScales");

    assertOk(role, response, commonPermissions, CommonPermissions.CREATE_GRADINGSCALE, 200);

    if (response.statusCode() == 200) {
      Long id = response.body().jsonPath().getLong("id");
      if (id != null) {
        given().headers(getAdminAuthHeaders()).delete(
            "/common/gradingScales/{ID}?permanent=true", id);
      }
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsListGradingScales(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role)).get(
        "/common/gradingScales");
    assertOk(role, response, commonPermissions, CommonPermissions.LIST_GRADINGSCALES,
        200);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsFindGradingScale(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role)).get(
        "/common/gradingScales/{ID}", 1);
    assertOk(role, response, commonPermissions, CommonPermissions.FIND_GRADINGSCALE,
        200);
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsUpdateGradingScale(Role role) throws NoSuchFieldException {
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

      Response updateResponse = given().headers(getAuthHeaders(role))
          .contentType("application/json").body(updateScale)
          .put("/common/gradingScales/{ID}", id);

      assertOk(role, updateResponse, commonPermissions,
          CommonPermissions.UPDATE_GRADINGSCALE, 200);

    } finally {
      given().headers(getAdminAuthHeaders()).delete(
          "/common/gradingScales/{ID}?permanent=true", id);
    }
  }

  @ParameterizedTest
  @EnumSource(Role.class)
  public void testPermissionsDeleteGradingScale(Role role) throws NoSuchFieldException {
    GradingScale gradingScale = new GradingScale(null, "to be deleted", "grading scale to be deleted", Boolean.FALSE);

    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(gradingScale)
      .post("/common/gradingScales");

    Long id = response.body().jsonPath().getLong("id");

    try {
      Response deleteResponse = given().headers(getAuthHeaders(role))
          .delete("/common/gradingScales/{ID}", id);
      assertOk(role, deleteResponse, commonPermissions, CommonPermissions.DELETE_GRADINGSCALE, 204);
    } finally {
      given().headers(getAdminAuthHeaders()).delete(
          "/common/gradingScales/{ID}?permanent=true", id);
    }  
  }
}
