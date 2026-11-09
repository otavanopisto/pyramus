package fi.otavanopisto.pyramus.plugin.googleoauth.scribe;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class GoogleApi20 extends DefaultApi20 {

  public static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth";
  public static final String TOKEN_URI = "https://accounts.google.com/o/oauth2/token";

  @Override
  public String getAccessTokenEndpoint() {
    return TOKEN_URI;
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return AUTHORIZATION_URL;
  }
  
}
