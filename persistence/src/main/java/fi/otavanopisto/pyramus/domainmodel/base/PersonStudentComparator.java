package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.Comparator;
import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

public class PersonStudentComparator implements Comparator<Student> {
  
  /**
   * Ordering study programmes as follows
   *  1. studies that have start date but no end date (ongoing)
   *  2. studies that have no start nor end date
   *  3. studies that have ended
   *  4. studies that are archived
   *  5. other
   */
  @Override
  public int compare(Student o1, Student o2) {
    int o1Value = getLatestStudentOrderValue(o1);
    int o2Value = getLatestStudentOrderValue(o2);
    if (o1Value == o2Value) {
      Date o1StudyStart = o1.getStudyStartDate();
      Date o2StudyStart = o2.getStudyStartDate(); 
      return o1StudyStart != null && o2StudyStart != null ? o2StudyStart.compareTo(o1StudyStart) : 0;
    }
    
    return o1Value < o2Value ? -1 : 1;
  }

  private int getLatestStudentOrderValue(Student student) {
    if (student.getArchived()) {
      return 4;
    }
    
    if (student.getStudyStartDate() != null && student.getStudyEndDate() == null) {
      return 1;
    }
    
    if (student.getStudyStartDate() == null && student.getStudyEndDate() == null) {
      return 2;
    }
    
    if (student.getStudyEndDate() != null) {
      return 3;
    }
    
    return 5;
  }
}