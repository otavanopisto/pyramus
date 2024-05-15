package fi.otavanopisto.pyramus.domainmodel.grading;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class ProjectAssessment extends Credit {
  
  public ProjectAssessment() {
    super();
    setCreditType(CreditType.ProjectAssessment);
  }
  
  public void setStudentProject(StudentProject studentProject) {
    this.studentProject = studentProject;
  }

  public StudentProject getStudentProject() {
    return studentProject;
  }

  @Transient
  public Student getStudent() {
    return getStudentProject() != null ? getStudentProject().getStudent() : null;
  }
  
  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name="studentProject")
  private StudentProject studentProject;
}