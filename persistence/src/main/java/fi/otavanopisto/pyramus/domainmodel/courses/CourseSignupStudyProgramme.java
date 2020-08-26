package fi.otavanopisto.pyramus.domainmodel.courses;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "course", "studyProgramme" })
    }
)
public class CourseSignupStudyProgramme {

  public Long getId() {
    return id;
  }
  
  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public StudyProgramme getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(StudyProgramme studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.IDENTITY)  
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="course")
  private Course course;
  
  @ManyToOne
  @JoinColumn(name="studyProgramme")
  private StudyProgramme studyProgramme;
  
}