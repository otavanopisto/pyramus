package fi.otavanopisto.pyramus.schedulers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;

@Startup
@Singleton
public class PastStudentsFilesCleanupScheduler {

  private static final int MAX_STUDENTS_BATCH = 50;
  
  @Inject
  private Logger logger;
  
  @Inject
  private StudentFileDAO studentFileDAO;

  @Schedule(dayOfWeek = "*", hour="8", minute="30", persistent = false)
  public void cleanup() {
    Date threshold = PastStudentDataThreshold.primaryThreshold();

    // List students who have ended before the threshold and have files
    List<Student> students = studentFileDAO.listPastStudentsWithFiles(threshold);
    int total = students.size();
    // Limit to max batch size
    students = students.size() > MAX_STUDENTS_BATCH ? students.subList(0, MAX_STUDENTS_BATCH) : students;

    for (Student student : students) {
      // List all files, archived or not
      List<StudentFile> studentFiles = studentFileDAO.listByStudent(student, TSB.IGNORE);
      logger.info(String.format("Removing %d files from student %d.", studentFiles.size(), student.getId()));
      
      for (StudentFile studentFile : studentFiles) {
        try {
          // Attempt to delete data ..
          if (PyramusFileUtils.deleteFileData(studentFile)) {
            try {
              // .. and if successful, delete entity
              studentFileDAO.delete(studentFile);
            } catch (Exception e) {
              logger.log(Level.SEVERE, String.format("Deleting StudentFile %d/%s failed although data was deleted.", studentFile.getId(), studentFile.getFileId()), e);
            }
          } else {
            logger.log(Level.SEVERE, String.format("Deleting file data of StudentFile %d/%s failed - no data was found.", studentFile.getId(), studentFile.getFileId()));
          }
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Deleting file data failed.", e);
        }
      }
    }
    
    logger.info(String.format("Handled %d/%d past students. Threshold was %tF.", students.size(), total, threshold));
  }
  
}
