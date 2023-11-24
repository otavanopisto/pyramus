package fi.otavanopisto.pyramus.schedulers.shredder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.file.StudentFile;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.schedulers.PastStudentDataThreshold;

public class PastStudentFileShredder implements ShredderTask {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentFileDAO studentFileDAO;
  
  @Override
  public Date thresholdDate() {
    return PastStudentDataThreshold.primaryThreshold();
  }
  
  @Override
  public List<Student> suggest(int maxStudents) {
    return studentFileDAO.listPastStudentsWithFiles(thresholdDate(), maxStudents);
  }
  
  @Override
  public void shred(Student student) {
    // Make sure given student matches the threshold
    if (!PastStudentDataThreshold.matchesThreshold(student, thresholdDate())) {
      return;
    }

    // List all files, archived or not
    List<StudentFile> studentFiles = studentFileDAO.listByStudent(student, TSB.IGNORE);
    
    if (CollectionUtils.isNotEmpty(studentFiles)) {
      logger.info(String.format("Removing %d file(s) from student %d.", studentFiles.size(), student.getId()));
      
      for (StudentFile studentFile : studentFiles) {
        try {
          // Attempt to delete data ..
          if (!PyramusFileUtils.deleteFile(studentFile)) {
            logger.log(Level.SEVERE, String.format("Deleting StudentFile %d/%s failed.", studentFile.getId(), studentFile.getFileId()));
          }
        } catch (IOException e) {
          logger.log(Level.SEVERE, "Deleting file data failed.", e);
        }
      }
    }
  }
  
}
