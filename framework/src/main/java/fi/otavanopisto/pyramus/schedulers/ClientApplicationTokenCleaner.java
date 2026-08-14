package fi.otavanopisto.pyramus.schedulers;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAccessTokenDAO;
import fi.otavanopisto.pyramus.dao.clientapplications.ClientApplicationAuthorizationCodeDAO;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAccessToken;
import fi.otavanopisto.pyramus.domainmodel.clientapplications.ClientApplicationAuthorizationCode;
import fi.otavanopisto.pyramus.domainmodel.users.Role;

@Singleton
@Startup
public class ClientApplicationTokenCleaner {
  
  private static final int BATCH_SIZE = 5000;

  @Inject
  private Logger logger;
  
  @Inject
  private ClientApplicationAccessTokenDAO clientApplicationAccessTokenDAO;
  
  @Inject
  private ClientApplicationAuthorizationCodeDAO clientApplicationAuthorizationCodeDAO;
  
  @Schedule(dayOfWeek = "*", hour = "*", minute = "*/5", persistent = false)
  private void removeExpiredTokens() {
    int removedTokens = 0;
    int removedCodes = 0;
    
    Instant tokenExpiryThreshold = Instant.now().minus(ClientApplicationAccessToken.REFRESHTOKEN_LIFETIME);
    
    List<ClientApplicationAccessToken> tokens = clientApplicationAccessTokenDAO.listByExpired(tokenExpiryThreshold, BATCH_SIZE);
    if (tokens.size() == BATCH_SIZE) {
      logger.warning("Client application access tokens possibly piling up");
    }
    
    for (ClientApplicationAccessToken token : tokens) {
      clientApplicationAccessTokenDAO.delete(token);
      removedTokens++;
    }

    Instant authCodeExpiryThreshold = Instant.now().minus(ClientApplicationAuthorizationCode.AUTHCODE_LIFETIME);
    List<ClientApplicationAuthorizationCode> expiredCodes = clientApplicationAuthorizationCodeDAO.listExpired(authCodeExpiryThreshold);
    for (ClientApplicationAuthorizationCode authCode : expiredCodes) {
      if (!authCode.getUser().hasRole(Role.TRUSTED_SYSTEM)) {
        clientApplicationAuthorizationCodeDAO.delete(authCode);
        removedCodes++;
      }
    }

    if (removedCodes > 0 || removedTokens > 0) {
      logger.info(String.format("Removed %d expired client application access tokens and %d expired codes", removedTokens, removedCodes));
    }
  }

}
