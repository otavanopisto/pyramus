package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.rest.controller.permissions.LanguagePermissions;
import fi.otavanopisto.pyramus.rest.model.Language;
import io.restassured.response.Response;

public class LanguagePermissionTestsIT extends AbstractRESTPermissionsTestJUnit5 {

  private LanguagePermissions languagePermissions = new LanguagePermissions();
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testCreateLanguage(Role role) throws NoSuchFieldException {
    Language language = new Language(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders(role))
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    assertOk(role, response, languagePermissions, LanguagePermissions.CREATE_LANGUAGE);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/languages/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testListLanguages(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/languages");

    assertOk(role, response, languagePermissions, LanguagePermissions.LIST_LANGUAGES);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testFindLanguage(Role role) throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders(role))
      .get("/students/languages/{ID}", 1);

    assertOk(role, response, languagePermissions, LanguagePermissions.FIND_LANGUAGE);
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testUpdateLanguage(Role role) throws NoSuchFieldException {
    Language language = new Language(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Language updateLanguage = new Language(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders(role))
        .contentType("application/json")
        .body(updateLanguage)
        .put("/students/languages/{ID}", id);
      
      assertOk(role, response, languagePermissions, LanguagePermissions.UPDATE_LANGUAGE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/languages/{ID}?permanent=true", id);
    }
  }
  
  @ParameterizedTest
  @EnumSource(Role.class)
  public void testDeleteLanguage(Role role) throws NoSuchFieldException {
    Language language = new Language(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");
    
    Long id = response.body().jsonPath().getLong("id");

    response = given().headers(getAuthHeaders(role))
      .delete("/students/languages/{ID}", id);

    assertOk(role, response, languagePermissions, LanguagePermissions.DELETE_LANGUAGE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/languages/{ID}?permanent=true", id);
  }
}
