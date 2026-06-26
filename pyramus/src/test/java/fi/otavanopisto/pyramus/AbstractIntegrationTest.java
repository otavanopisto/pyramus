package fi.otavanopisto.pyramus;

import static io.restassured.RestAssured.certificate;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.ClientID;

import fi.otavanopisto.pyramus.domainmodel.users.Role;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.response.Response;

public abstract class AbstractIntegrationTest {

  @Rule
  public TestName testName = new TestName();

  private static Map<Role, String> ROLE_TOKENS = new HashMap<>();

  static {
    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.useRelaxedHTTPSValidation();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());

    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
        ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
          @Override
          public com.fasterxml.jackson.databind.ObjectMapper create(Type cls, String charset) {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper;
          }
        }));
  }

  @Before
  public void baseSetupSql() throws Exception {
    String methodName = testName.getMethodName();
    int paramIndex = methodName.indexOf('[');
    if (paramIndex > 0) {
      methodName = methodName.substring(0, paramIndex);
    }
    Method method = getClass().getMethod(methodName, new Class<?>[] {});
    SqlBefore annotation = method.getAnnotation(SqlBefore.class);
    if (annotation != null) {
      String[] sqlFiles = annotation.value();

      if (sqlFiles != null && sqlFiles.length > 0) {
        Connection connection = getConnection();
        try {
          for (String sqlFile : sqlFiles) {
            runSql(connection, sqlFile);
          }
          connection.commit();
        } finally {
          connection.close();
        }
      }
    }
  }
  
  @After
  public void baseTearDownSql() throws Exception {
    String methodName = testName.getMethodName();
    int paramIndex = methodName.indexOf('[');
    if (paramIndex > 0) {
      methodName = methodName.substring(0, paramIndex);
    }
    Method method = getClass().getMethod(methodName, new Class<?>[] {});
    SqlAfter annotation = method.getAnnotation(SqlAfter.class);
    if (annotation != null) {
      String[] sqlFiles = annotation.value();

      if (sqlFiles != null && sqlFiles.length > 0) {
        Connection connection = getConnection();
        try {
          for (String sqlFile : sqlFiles) {
            runSql(connection, sqlFile);
          }
          connection.commit();
        } finally {
          connection.close();
        }
      }
    }
  }

  protected int getEntityCount(String entity) throws SQLException, ClassNotFoundException {
    Connection connection = getConnection();
    Statement statement = connection.createStatement();
    statement.execute("select count(*) as c from " + entity);
    
    ResultSet rs = statement.getResultSet();
    if (rs.next())
      return rs.getInt(1);
    
    return 0;
  }
  
  private void runSql(Connection connection, String file) throws IOException, SQLException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream sqlStream = classLoader.getResourceAsStream(file);
    if (sqlStream != null) {
      try {
        String sqlString = IOUtils.toString(sqlStream);
        Pattern commentPattern = Pattern.compile("--.*$", Pattern.MULTILINE);
        sqlString = commentPattern.matcher(sqlString).replaceAll("");
        String[] sqls = sqlString.split(";(?=([^\']*\'[^\']*\')*[^\']*$)"); // Quote-aware split on ';'
        for (String sql : sqls) {
          sql = sql.trim();
          if (StringUtils.isNotBlank(sql)) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
          }
        }
      } finally {
        sqlStream.close();
      }
    } else {
      throw new FileNotFoundException(file);
    }
  }

  protected Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName(getJdbcDriver());
    return DriverManager.getConnection(getJdbcUrl(), getJdbcUsername(), getJdbcPassword());
  }

  protected static String getAppUrl() {
    return getAppUrl(false);
  }

  protected static String getAppUrl(boolean secure) {
    return (secure ? "https://" : "http://") + getHost() + ':' + (secure ? getPortHttps() : getPortHttp());
  }

  protected static String getJdbcDriver() {
    return System.getProperty("it.jdbc.driver");
  }

  protected static String getJdbcUrl() {
    return System.getProperty("it.jdbc.url");
  }

  protected static String getJdbcJndi() {
    return System.getProperty("it.jdbc.jndi");
  }

  protected static String getJdbcUsername() {
    return System.getProperty("it.jdbc.username");
  }

  protected static String getJdbcPassword() {
    return System.getProperty("it.jdbc.password");
  }

  protected static String getHost() {
    return System.getProperty("it.host");
  }

  protected static int getPortHttp() {
    return Integer.parseInt(System.getProperty("it.port.http"));
  }

  protected static int getPortHttps() {
    return Integer.parseInt(System.getProperty("it.port.https"));
  }

  protected static String getKeystoreFile() {
    return System.getProperty("it.keystore.file");
  }

  protected static String getKeystoreAlias() {
    return System.getProperty("it.keystore.alias");
  }

  protected static String getKeystorePass() {
    return System.getProperty("it.keystore.storepass");
  }

  protected String getBrowser() {
    String browser = System.getProperty("it.browser");
    if (browser != null) {
      return browser;
    }
    return "";
  }

  protected String getOAuthAccessToken(Role role) {
    if (ROLE_TOKENS.containsKey(role)) {
      return ROLE_TOKENS.get(role);
    }
    else {
      URI callback;
      URI tokenEndpoint;
  
      try {
        callback = new URI(fi.otavanopisto.pyramus.Common.REDIRECT_URL);
        tokenEndpoint = new URI(fi.otavanopisto.pyramus.Common.TOKEN_URI);
      } 
      catch (URISyntaxException e) {
        throw new RuntimeException("Invalid test login URIs.");
      }
    
      AuthorizationCode code = new AuthorizationCode(fi.otavanopisto.pyramus.Common.getRoleAuth(role));
      AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);
  
      // The credentials to authenticate the client at the token endpoint
      ClientID clientID = new ClientID(fi.otavanopisto.pyramus.Common.CLIENT_ID);
      Secret clientSecret = new Secret(fi.otavanopisto.pyramus.Common.CLIENT_SECRET);
      ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
  
      // The token endpoint
  
      Scope scope = new Scope("legacy");
      
      // Make the token request
      TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, codeGrant, scope);
      HTTPRequest nimbusHttpRequest = request.toHTTPRequest();
      
      String authHeader = nimbusHttpRequest.getAuthorization();
      String tokenRequestBody = nimbusHttpRequest.getBody();

      // We're making the call with RestAssured because it's already set up to work 
      // with the self-signed certificates, which seems to be pain in the behind to 
      // get working with the Nimbus framework - we can look into it later
      
      Response response = given().contentType("application/x-www-form-urlencoded").header("Authorization", authHeader).body(tokenRequestBody)
          .post("/oauth/token");
      
      response.then().statusCode(200);
      
      String accessToken = response.body().jsonPath().getString("access_token");
      ROLE_TOKENS.put(role, accessToken);
      return accessToken;
    }
  }
  
  protected OffsetDateTime getDateToOffsetDateTime(int year, int monthOfYear, int dayOfMonth) {
    LocalDateTime localDateTime = LocalDateTime.of(year, monthOfYear, dayOfMonth, 0, 0);
    ZoneId systemId = ZoneId.systemDefault();
    ZoneOffset offset = systemId.getRules().getOffset(localDateTime);
    return localDateTime.atOffset(offset);
  }
  
  public OffsetDateTime getDate(int year, int monthOfYear, int dayOfMonth) {
    return getDateToOffsetDateTime(year, monthOfYear, dayOfMonth);
  }

  public String getDateString(int year, int monthOfYear, int dayOfMonth) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    return getDateToOffsetDateTime(year, monthOfYear, dayOfMonth).format(dateFormatter);
  }
  
  public LocalDate toLocalDate(Date date) {
    return date != null
        ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        : null;
  }
  
  public LocalDate toLocalDate(OffsetDateTime offsetDateTime) {
    return offsetDateTime != null
        ? offsetDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        : null;
  }
  
}
