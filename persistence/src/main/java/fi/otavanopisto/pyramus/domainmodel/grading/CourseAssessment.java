package fi.otavanopisto.pyramus.domainmodel.grading;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CourseAssessment extends Credit {
  
  public CourseAssessment() {
    super();
    setCreditType(CreditType.CourseAssessment);
  }
  
  @Transient
  public Student getStudent() {
    return courseStudent != null ? courseStudent.getStudent() : null;
  }
  
  @Transient
  public Subject getSubject() {
    return (getCourseStudent() != null && getCourseStudent().getCourse() != null) 
        ? getCourseStudent().getCourse().getSubject() : null; 
  }
  
  @Transient
  public Integer getCourseNumber() {
    return (getCourseStudent() != null && getCourseStudent().getCourse() != null) 
        ? getCourseStudent().getCourse().getCourseNumber() : null; 
  }
  
  @Transient
  public EducationalLength getCourseLength() {
    return (getCourseStudent() != null && getCourseStudent().getCourse() != null) 
        ? getCourseStudent().getCourse().getCourseLength() : null; 
  }
  
  public void setCourseStudent(CourseStudent courseStudent) {
    this.courseStudent = courseStudent;
  }
  
  public CourseStudent getCourseStudent() {
    return courseStudent;
  }

  @ManyToOne
  @JoinColumn(name="courseStudent")
  private CourseStudent courseStudent;
}