package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.certificate;

import org.junit.Before;

import com.jayway.restassured.RestAssured;


public abstract class AbstractRESTServiceTest {
  
  @Before
  public void setupRestAssured() {
    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());
  }

  protected String getAppUrl() {
    return getAppUrl(false);
  }
  
  protected String getAppUrl(boolean secure) {
    return (secure ? "https://" : "http://") + getHost() + ':' + (secure ? getPortHttps() : getPortHttp());
  }

  protected String getHost() {
    return System.getProperty("it.host");
  }

  protected int getPortHttp() {
    return Integer.parseInt(System.getProperty("it.port.http"));
  }

  protected int getPortHttps() {
    return Integer.parseInt(System.getProperty("it.port.https"));
  }
  
  protected String getKeystoreFile() {
    return System.getProperty("it.keystore.file");
  }
  
  protected String getKeystoreAlias() {
    return System.getProperty("it.keystore.alias");
  }
  
  protected String getKeystorePass() {
    return System.getProperty("it.keystore.storepass");
  }
  
}
