package fi.otavanopisto.pyramus.schedulers;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;

import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

/**
 * Due to how Persons are indexed with the activity status determined
 * at the indexing time, the activity doesn't automatically update in 
 * time. This scheduler does its best to reindex the students for who 
 * the activity status has changed i.e. those who have started 'today' 
 * or have ended studies 'yesterday'. This should be run early to the 
 * day to have the status fixed for the search functions.
 */
@Singleton
@Startup
public class StudentActiveStatusReindexScheduler {
  
  @Inject
  private Logger logger;
  
  @Inject
  private PersonDAO personDAO;
  
  @Inject
  private StudentDAO studentDAO;

  @Schedule(dayOfWeek = "*", hour = "4", minute = "0", persistent = false)
  private void reindexStudentsWithActivityChange() {
    /**
     * Study start/end dates are saved as dates so this seems 
     * to work ok with Criteria. It's easy to see though that
     * JPA implementation may play some role whether the time
     * component of the date is going to mess things up.
     */
    Date today = new Date();
    Date yesterday = DateUtils.addDays(today, -1);

    /**
     * Students that have started with current date
     */
    List<Student> studentsStartedToday = studentDAO.listUnarchivedByStudyStartDate(today);
    
    if (CollectionUtils.isNotEmpty(studentsStartedToday)) {
      logger.info(String.format("Reindexing %d students who have started today", studentsStartedToday.size()));
      
      for (Student student : studentsStartedToday) {
        personDAO.forceReindex(student.getPerson());
      }
    }

    /**
     * Students that have ended studies a day before
     */
    List<Student> studentsEndedYesterday = studentDAO.listUnarchivedByStudyEndDate(yesterday);

    if (CollectionUtils.isNotEmpty(studentsEndedYesterday)) {
      logger.info(String.format("Reindexing %d students who have ended yesterday", studentsEndedYesterday.size()));

      for (Student student : studentsEndedYesterday) {
        personDAO.forceReindex(student.getPerson());
      }
    }

    logger.info("Student activity reindexer done.");
  }

}
