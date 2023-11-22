package fi.otavanopisto.pyramus.schedulers.shredder;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.otavanopisto.pyramus.schedulers.PastStudentDataThreshold;

public class PastStudentContactLogShredder implements ShredderTask {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentContactLogEntryDAO studentContactLogEntryDAO;
  
  @Inject
  private StudentContactLogEntryCommentDAO studentContactLogEntryCommentDAO;
  
  @Override
  public Date thresholdDate() {
    return PastStudentDataThreshold.primaryThreshold();
  }
  
  @Override
  public List<Student> suggest(int maxStudents) {
    return studentContactLogEntryDAO.listPastStudentsWithContactEntries(thresholdDate(), maxStudents);
  }
  
  @Override
  public void shred(Student student) {
    // Make sure given student matches the threshold
    if (!PastStudentDataThreshold.matchesThreshold(student, thresholdDate())) {
      return;
    }

    // List all contact log entries, archived or not
    List<StudentContactLogEntry> studentContactLogEntries = studentContactLogEntryDAO.listByStudent(student, TSB.IGNORE);

    if (CollectionUtils.isNotEmpty(studentContactLogEntries)) {
      logger.info(String.format("Removing %d contact log entries from student %d.", studentContactLogEntries.size(), student.getId()));
      
      for (StudentContactLogEntry contactLogEntry : studentContactLogEntries) {
        List<StudentContactLogEntryComment> contactLogEntryComments = studentContactLogEntryCommentDAO.listByEntry(contactLogEntry, TSB.IGNORE);
        contactLogEntryComments.forEach(contactLogEntryComment -> studentContactLogEntryCommentDAO.delete(contactLogEntryComment));
        
        studentContactLogEntryDAO.delete(contactLogEntry);
      }
    }
  }
  
}
