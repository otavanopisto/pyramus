package fi.otavanopisto.pyramus.domainmodel.courses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "course", "studentGroup" })
    }
)
public class CourseSignupStudentGroup {

  public Long getId() {
    return id;
  }
  
  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.IDENTITY)  
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="course")
  private Course course;
  
  @ManyToOne
  @JoinColumn(name="studentGroup")
  private StudentGroup studentGroup;
  
}