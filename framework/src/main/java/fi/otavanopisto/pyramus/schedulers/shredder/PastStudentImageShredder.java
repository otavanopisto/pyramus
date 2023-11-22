package fi.otavanopisto.pyramus.schedulers.shredder;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentImageDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentImage;
import fi.otavanopisto.pyramus.schedulers.PastStudentDataThreshold;

public class PastStudentImageShredder implements ShredderTask {

  @Inject
  private Logger logger;
  
  @Inject
  private StudentImageDAO studentImageDAO;
  
  @Override
  public Date thresholdDate() {
    return PastStudentDataThreshold.primaryThreshold();
  }
  
  @Override
  public List<Student> suggest(int maxStudents) {
    return studentImageDAO.listPastStudentsWithImage(thresholdDate(), maxStudents);
  }
  
  @Override
  public void shred(Student student) {
    // Make sure given student matches the threshold
    if (!PastStudentDataThreshold.matchesThreshold(student, thresholdDate())) {
      return;
    }

    StudentImage studentImage = studentImageDAO.findByStudent(student);

    if (studentImage != null) {
      logger.info(String.format("Removing image from student %d.", student.getId()));
      studentImageDAO.delete(studentImage);
    }
  }
  
}
