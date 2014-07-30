package fi.pyramus.rest;

import static com.jayway.restassured.RestAssured.certificate;

import org.joda.time.DateTime;
import org.junit.Before;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;

public abstract class AbstractRESTServiceTest {
  
  @Before
  public void setupRestAssured() {
    RestAssured.baseURI = getAppUrl(true) + "/1";
    RestAssured.port = getPortHttps();
    RestAssured.authentication = certificate(getKeystoreFile(), getKeystorePass());
    
    RestAssured.config = RestAssuredConfig.config().objectMapperConfig(ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(
      new Jackson2ObjectMapperFactory() {
        
        @SuppressWarnings("rawtypes")
        @Override
        public com.fasterxml.jackson.databind.ObjectMapper create(Class cls, String charset) {
          com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
          objectMapper.registerModule(new JodaModule());
          objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
          return objectMapper;
        }
      }
    ));
    
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
  
  protected DateTime getDate(int year, int monthOfYear, int dayOfMonth) {
    return new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0, 0);
  }
}
