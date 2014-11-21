package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.response.Response;

import fi.pyramus.rest.model.EducationType;

@RunWith(Parameterized.class)
public class EducationTypePermissionsTestsIT extends AbstractRESTPermissionsTest {
  @Parameters
  public static List<Object[]> generateData() {
    return getGeneratedRoleData();
  }
  public EducationTypePermissionsTestsIT(String role) {
    super(role);
  }

  @Test
  public void testPermissionsCreateEducationType() {
    EducationType educationType = new EducationType(null, "create type", "TST", Boolean.FALSE);
    
    Response response = given().headers(getAuthHeaders())
      .contentType("application/json")
      .body(educationType)
      .post("/common/educationTypes");

    
    response.then().statusCode(200);
    response.then()
    .body("id", not(is((Long) null)))      
    int id = response.body().jsonPath().getInt("id");

      given().headers(getAuthHeaders())
      .delete("/common/educationTypes/{ID}?permanent=true", id);

  }
  
}
