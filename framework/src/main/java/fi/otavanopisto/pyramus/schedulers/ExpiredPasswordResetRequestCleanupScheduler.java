package fi.otavanopisto.pyramus.schedulers;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.users.PasswordResetRequestDAO;

@Startup
@Singleton
public class ExpiredPasswordResetRequestCleanupScheduler {
  
  @Inject
  private Logger logger;
  
  @Inject
  private PasswordResetRequestDAO passwordResetRequestDAO;

  @Schedule(dayOfWeek = "*", hour="9", persistent = false)
  public void cleanup() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR, -2);
    Date expiryDate = cal.getTime();

    long total = passwordResetRequestDAO.count();
    int countExpired = passwordResetRequestDAO.deleteExpired(expiryDate);
    
    logger.info(String.format("Removed %d/%d expired password reset requests.", countExpired, total));
  }
  
}
