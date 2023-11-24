package fi.otavanopisto.pyramus.schedulers.shredder;

import java.util.Date;
import java.util.List;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

public interface ShredderTask {
  
  /**
   * Return the threshold date the task follows. shred function
   * should do nothing if the Student's studyEndDate is after this.
   * 
   * @return the threshold date
   */
  Date thresholdDate();
  
  /**
   * "Suggest" up to maxStudents number of students to process.
   * 
   * Make sure that any Student that is listed here is not listed
   * anymore after a call to shred(student).
   * 
   * @param maxStudents maximum number of students to list
   * @return list of students
   */
  List<Student> suggest(int maxStudents);

  /**
   * Shred the information for given student this ShredderTask is 
   * responsible for.
   * 
   * @param student Student
   */
  void shred(Student student);
  
}
