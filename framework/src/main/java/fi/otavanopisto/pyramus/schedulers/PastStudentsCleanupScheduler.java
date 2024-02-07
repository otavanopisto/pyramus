package fi.otavanopisto.pyramus.schedulers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;
import fi.otavanopisto.pyramus.framework.SettingUtils;
import fi.otavanopisto.pyramus.schedulers.shredder.ShredderTask;

@Startup
@Singleton
public class PastStudentsCleanupScheduler {

  private static final int MAX_STUDENTS_BATCHSIZE_DEFAULT = 50;
  private static final String PASTSTUDENTSCLEANUPSHCEDULER_BATCHSIZE_VARIABLE = "paststudentscleanupscheduler.batchSize";
  private static final String PASTSTUDENTSCLEANUPSHCEDULER_ENABLED_VARIABLE = "paststudentscleanupscheduler.enabled";
  
  @Inject
  private Logger logger;
  
  @Inject
  @Any
  private Instance<ShredderTask> shredderTasks;

  @Inject
  private SettingDAO settingDAO;

  @Inject
  private SettingKeyDAO settingKeyDAO;

  @Schedule(dayOfWeek = "*", hour="8", minute="30", persistent = false)
  public void cleanup() {
    if (!isEnabled()) {
      return;
    }
    
    int maxStudents = getBatchSize();
    
    for (ShredderTask shredderSuggester : shredderTasks) {
      List<Student> students = shredderSuggester.suggest(maxStudents);
      
      for (Student student : students) {
        /**
         * Technically speaking we shouldn't need to run all the
         * shredders here, especially the ones that didn't suggest
         * any Students before the current suggester. However maybe
         * this isn't a big enough problem to try and solve.
         */
        for (ShredderTask shredder : shredderTasks) {
          if (isValid(student, shredder)) {
            shredder.shred(student);
          }
        }
      }
      
      maxStudents -= students.size();
      
      if (maxStudents <= 0) {
        break;
      }
    }
  }

  /**
   * Container startup variable initialization
   */
  @PostConstruct
  private void setupSettings() {
    // If the master switch doesn't exist, create it as disabled by default
    if (settingKeyDAO.findByName(PASTSTUDENTSCLEANUPSHCEDULER_ENABLED_VARIABLE) == null) {
      SettingKey settingKey = settingKeyDAO.create(PASTSTUDENTSCLEANUPSHCEDULER_ENABLED_VARIABLE);
      settingDAO.create(settingKey, "false");
    }
    
    // Create batchSize variable with default value if it doesn't exist
    if (settingKeyDAO.findByName(PASTSTUDENTSCLEANUPSHCEDULER_BATCHSIZE_VARIABLE) == null) {
      SettingKey settingKey = settingKeyDAO.create(PASTSTUDENTSCLEANUPSHCEDULER_BATCHSIZE_VARIABLE);
      settingDAO.create(settingKey, String.valueOf(MAX_STUDENTS_BATCHSIZE_DEFAULT));
    }
  }

  /**
   * Returns true if this scheduler is enabled.
   * 
   * @return true if this scheduler is enabled
   */
  private boolean isEnabled() {
    String value = SettingUtils.getSettingValue(PASTSTUDENTSCLEANUPSHCEDULER_ENABLED_VARIABLE);
    return Boolean.parseBoolean(value);
  }

  /**
   * Returns batch size from variable.
   * 
   * @return batch size from variable
   */
  private int getBatchSize() {
    String value = SettingUtils.getSettingValue(PASTSTUDENTSCLEANUPSHCEDULER_BATCHSIZE_VARIABLE);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException nfe) {
      return MAX_STUDENTS_BATCHSIZE_DEFAULT;
    }
  }

  /**
   * Does basic validity checks for the student including
   * - the student cannot be null
   * - the student must have a study end date
   * - the student's study end date must match (be older than) a threshold
   * (these will log a warning and return false if any fail)
   * 
   * Also
   * - the student's study end date must match the shredder's threshold
   * (which is a legit skip situation because the student may be listed
   * from a short-threshold strategy and thus longer threshold strategies
   * have no interest on it)
   * 
   * @param student
   * @param shredder
   * @return
   */
  private boolean isValid(Student student, ShredderTask shredder) {
    if (student == null) {
      logger.log(Level.WARNING, "Student was null");
      return false;
    }
    
    // studyEndDate must exist and it must be before one of the thresholds
    if (student.getStudyEndDate() == null || !PastStudentDataThreshold.matchesAnyThreshold(student.getStudyEndDate())) {
      logger.log(Level.WARNING, String.format("Student's %d studyEndDate doesn't exist or it doesn't match any threshold.", student.getId()));
      return false;
    }

    // Legit skip situation when f.ex student is listed by shredder 
    // that has shorter threshold date than given shredder
    if (student.getStudyEndDate().after(shredder.thresholdDate())) {
      return false;
    }
    
    return true;
  }
  
}
