package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.restassured.response.Response;

import fi.otavanopisto.pyramus.rest.controller.permissions.LanguagePermissions;
import fi.otavanopisto.pyramus.rest.model.Language;

@RunWith(Parameterized.class)
public class LanguagePermissionTestsIT extends AbstractRESTPermissionsTest {

  public LanguagePermissionTestsIT(String role) {
    this.role = role;
  }
  
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  
  private LanguagePermissions languagePermissions = new LanguagePermissions();
  
  @Test
  public void testCreateLanguage() throws NoSuchFieldException {
    Language language = new Language(null, "TST", "create", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    assertOk(response, languagePermissions, LanguagePermissions.CREATE_LANGUAGE);
      
    if (response.getStatusCode() == 200) {
      int id = response.body().jsonPath().getInt("id");
      
      given().headers(getAdminAuthHeaders())
        .delete("/students/languages/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testListLanguages() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/languages");

    assertOk(response, languagePermissions, LanguagePermissions.LIST_LANGUAGES);
  }
  
  @Test
  public void testFindLanguage() throws NoSuchFieldException {
    Response response = given().headers(getAuthHeaders())
      .get("/students/languages/{ID}", 1);

    assertOk(response, languagePermissions, LanguagePermissions.FIND_LANGUAGE);
  }
  
  @Test
  public void testUpdateLanguage() throws NoSuchFieldException {
    Language language = new Language(null, "Not Updated", "NOT", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");

    Long id = response.body().jsonPath().getLong("id");
    try {
      Language updateLanguage = new Language(id, "Updated", "UPD", Boolean.FALSE);

      response = given().headers(getAuthHeaders())
        .contentType("application/json")
        .body(updateLanguage)
        .put("/students/languages/{ID}", id);
      
      assertOk(response, languagePermissions, LanguagePermissions.UPDATE_LANGUAGE);
    } finally {
      given().headers(getAdminAuthHeaders())
        .delete("/students/languages/{ID}?permanent=true", id);
    }
  }
  
  @Test
  public void testDeleteLanguage() throws NoSuchFieldException {
    Language language = new Language(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAdminAuthHeaders())
      .contentType("application/json")
      .body(language)
      .post("/students/languages");
    
    Long id = response.body().jsonPath().getLong("id");

    response = given().headers(getAuthHeaders())
      .delete("/students/languages/{ID}", id);

    assertOk(response, languagePermissions, LanguagePermissions.DELETE_LANGUAGE, 204);

    given().headers(getAdminAuthHeaders())
      .delete("/students/languages/{ID}?permanent=true", id);
  }
}
