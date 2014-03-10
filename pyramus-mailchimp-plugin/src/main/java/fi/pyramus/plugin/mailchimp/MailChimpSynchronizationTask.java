package fi.pyramus.plugin.mailchimp;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpException;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.dao.system.SettingDAO;
import fi.pyramus.dao.system.SettingKeyDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.system.Setting;
import fi.pyramus.domainmodel.system.SettingKey;
import fi.pyramus.plugin.scheduler.ScheduledPluginTask;
import fi.pyramus.plugin.scheduler.ScheduledTaskException;
import fi.pyramus.plugin.scheduler.ScheduledTaskInterval;

public class MailChimpSynchronizationTask implements ScheduledPluginTask {
  
  private static Logger logger = Logger.getLogger(MailChimpSynchronizationTask.class.getName());

  @Override
  public ScheduledTaskInterval getInternal() {
    return ScheduledTaskInterval.HOUR;
  }

  @Override
  public void execute() throws ScheduledTaskException {
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    
    SettingKey apiKeySettingKey = settingKeyDAO.findByName("mailchimp.apiKey");
    if (apiKeySettingKey != null) {
      Setting apiKeySetting = settingDAO.findByKey(apiKeySettingKey);
      if (apiKeySetting != null && StringUtils.isNotBlank(apiKeySetting.getValue())) {
        logger.info("Synchronizing emails into MailChimp");

        List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
        MailChimpClient mailChimpClient = new MailChimpClient();
    
        try {
          for (StudyProgramme studyProgramme : studyProgrammes) {
            MailChimpSyncResult syncResult = MailChimpSyncUtils.synchronizeStudyProgramme(mailChimpClient, apiKeySetting.getValue(), studyProgramme);
            if (syncResult != null) {
              logger.info("Synchronized studyProgramme " + studyProgramme.getName() + " emails into MailChimp");

              for (MailChimpSyncError error : syncResult.getErrors()) {
                logger.warning("Error occurred while synchronizing '" + error.getEmail() + "': " + error.getError());
              }
              
              logger.info("Synchronization result: Emails added : " + syncResult.getAdded() + ", updated: " + syncResult.getUpdated() + ", removed: " + syncResult.getRemoved());
            }
          }
        } catch (IOException e) {
          throw new ScheduledTaskException("Unknown error occurred while synchronizing emails into MailChimp", e);
        } catch (MailChimpException e) {
          throw new ScheduledTaskException("Unknown error occurred while synchronizing emails into MailChimp", e);
        }
        
        logger.info("MailChimp synchronization complete");
      } else {
        logger.warning("MailChimp API key missing");
      }
    } else {
      logger.warning("MailChimp API key missing");
    }
  }

}
