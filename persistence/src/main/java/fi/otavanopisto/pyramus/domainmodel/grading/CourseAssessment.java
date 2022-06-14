package fi.otavanopisto.pyramus.domainmodel.grading;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
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
    return getCourseModule() != null ? getCourseModule().getSubject() : null; 
  }
  
  @Transient
  public Integer getCourseNumber() {
    return getCourseModule() != null ? getCourseModule().getCourseNumber() : null; 
  }
  
  @Transient
  public EducationalLength getCourseLength() {
    return getCourseModule() != null ? getCourseModule().getCourseLength() : null; 
  }
  
  public void setCourseStudent(CourseStudent courseStudent) {
    this.courseStudent = courseStudent;
  }
  
  public CourseStudent getCourseStudent() {
    return courseStudent;
  }

  public CourseModule getCourseModule() {
    return courseModule;
  }

  public void setCourseModule(CourseModule courseModule) {
    this.courseModule = courseModule;
  }

  @NotNull
  @ManyToOne
  @JoinColumn(name="courseStudent", nullable = false)
  private CourseStudent courseStudent;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name="courseModule", nullable = false)
  private CourseModule courseModule;
  
}