package fi.otavanopisto.pyramus.schedulers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
  
  private static final int BATCH_SIZE = 1000;

  @Inject
  private Logger logger;
  
  @Inject
  private ClientApplicationAccessTokenDAO clientApplicationAccessTokenDAO;
  
  @Inject
  private ClientApplicationAuthorizationCodeDAO clientApplicationAuthorizationCodeDAO;
  
  @Schedule(dayOfWeek = "*", hour="6", persistent = false)
  private void removeExpiredTokens() {
    int removed = 0;
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -1);
    long threshold = calendar.getTimeInMillis() / 1000;
    
    List<ClientApplicationAccessToken> tokens = clientApplicationAccessTokenDAO.listByExpired(threshold, BATCH_SIZE);
    if (tokens.size() == BATCH_SIZE) {
      logger.warning("Client application access tokens possibly piling up");
    }
    for (ClientApplicationAccessToken token : tokens) {
      ClientApplicationAuthorizationCode authCode = token.getClientApplicationAuthorizationCode();
      if (authCode.getUser().getRole() == Role.TRUSTED_SYSTEM) {
        continue;
      }
      clientApplicationAccessTokenDAO.delete(token);
      clientApplicationAuthorizationCodeDAO.delete(authCode);
      removed++;
    }

    if (removed > 0) {
      logger.info(String.format("Removed %d expired client application access tokens", removed));
    }
    
  }

}
