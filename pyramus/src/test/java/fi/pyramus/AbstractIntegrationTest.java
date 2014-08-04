package fi.pyramus;

import org.joda.time.DateTime;

public abstract class AbstractIntegrationTest {


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
