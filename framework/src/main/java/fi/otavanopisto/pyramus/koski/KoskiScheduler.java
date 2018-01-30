package fi.otavanopisto.pyramus.koski;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import fi.otavanopisto.pyramus.dao.koski.KoskiPersonLogDAO;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonLog;
import fi.otavanopisto.pyramus.domainmodel.koski.KoskiPersonState;

@Singleton
@Startup
@TransactionManagement(value=TransactionManagementType.BEAN)
public class KoskiScheduler {
 
  private static final long INITIAL_DELAY_SECONDS = 180;
  private static final long DELAY_SECONDS = 60;

  @Inject
  private Logger logger;
  
  @Inject
  private KoskiClient koskiClient;

  @Inject
  private KoskiSettings settings;
  
  @Inject
  private KoskiPersonLogDAO koskiPersonLogDAO;
  
  @Resource
  private ManagedScheduledExecutorService scheduledExecutorService;
  
  @Resource
  private UserTransaction userTransaction;
  
  @PostConstruct
  private void postConstruct() {
    scheduledExecutorService.scheduleWithFixedDelay(this::synchronize, INITIAL_DELAY_SECONDS, DELAY_SECONDS, TimeUnit.SECONDS);
  }
  
  private void synchronize() {
    try {
      userTransaction.begin();
      
      if (settings.isEnabled()) {
        KoskiPersonLog pending = koskiPersonLogDAO.findOldestByState(KoskiPersonState.PENDING);
        
        if (pending != null) {
          koskiClient.updatePerson(pending.getPerson());
        }
      }
      
      userTransaction.commit();
    } catch (Exception ex) {
      logger.log(Level.SEVERE, "Koski synchronization failed.", ex);
      
      try {
        userTransaction.rollback();
      } catch (Exception rbex) {
        logger.log(Level.SEVERE, "Transaction rollback failed.", ex);
      }
    }
  }
  
}
