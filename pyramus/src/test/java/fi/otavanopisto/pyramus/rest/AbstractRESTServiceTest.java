package fi.otavanopisto.pyramus.rest;

import static io.restassured.RestAssured.given;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;

import fi.otavanopisto.pyramus.AbstractIntegrationTest;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public abstract class AbstractRESTServiceTest extends AbstractIntegrationTest {

  public AbstractRESTServiceTest() {
    this.tools = new AbstractRESTServiceTestTools(new AbstractRestServicePermissionsTestI() {
      private AbstractRESTServiceTest pack;
      @Override
      public OffsetDateTime getDate(int year, int monthOfYear, int dayOfMonth) {
        return pack.getDate(year, monthOfYear, dayOfMonth);
      }
      @Override
      public Map<String, String> getAdminAuthHeaders() {
        return pack.getAuthHeaders();
      }
      private AbstractRestServicePermissionsTestI init(AbstractRESTServiceTest p) {
        this.pack = p;
        return this;
      }
    }.init(this));
  }
  
  @Before
  public void createAccessToken() {
    String accessToken = getOAuthAccessToken(Role.ADMINISTRATOR);
    setAccessToken(accessToken);
    
    setUserId(given().headers(getAuthHeaders())
        .contentType("application/json")
        .get("/system/whoami")
        .body().jsonPath().getLong("id"));
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Map<String, String> getAuthHeaders() {
    Map<String, String> headers = new HashMap<>();
    String accessToken = this.getAccessToken();
    if (StringUtils.isNotBlank(accessToken)) {
      headers.put("Authorization", "Bearer " + accessToken);
    }
    return headers;
  }

  public void login(int userid) {
    Response loginResponse = given() // Login first using dummy login method
        .contentType(ContentType.URLENC).param("testuserid", userid).post("https://dev.pyramus.fi:8443/users/externallogin.page");
    String jsessionId = loginResponse.getCookie("JSESSIONID");
    setSessionId(jsessionId);
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public Long getUserId() {
    return userId;
  }
  
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  protected AbstractRESTServiceTestTools tools() {
    return tools;
  }

  private AbstractRESTServiceTestTools tools;
  private String sessionId;
  private String accessToken;
  private Long userId;
}
