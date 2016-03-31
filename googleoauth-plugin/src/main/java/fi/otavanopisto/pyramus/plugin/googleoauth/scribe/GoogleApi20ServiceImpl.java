package fi.otavanopisto.pyramus.plugin.googleoauth.scribe;

import net.sf.json.JSONObject;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

public class GoogleApi20ServiceImpl extends OAuth20ServiceImpl {

  public GoogleApi20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
    super(api, config);

    this.api = api;
    this.config = config;
  }

  @Override
  public Token getAccessToken(Token requestToken, Verifier verifier) {
    OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
    request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
    request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
    request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
    request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
    request.addBodyParameter("grant_type", "authorization_code");
    if (config.hasScope())
      request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
    Response response = request.send();
    
    JSONObject tokenJson = JSONObject.fromObject(response.getBody());

    return api.getAccessTokenExtractor().extract(tokenJson.toString());
  }

  private final OAuthConfig config;
  private final DefaultApi20 api;
}
