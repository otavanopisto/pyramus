package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.ProjectAssessment.class, entityType = TranquilModelType.COMPLETE)
public class ProjectAssessmentComplete extends ProjectAssessmentBase {

  public TranquilModelEntity getGrade() {
    return grade;
  }

  public void setGrade(TranquilModelEntity grade) {
    this.grade = grade;
  }

  public TranquilModelEntity getAssessingUser() {
    return assessingUser;
  }

  public void setAssessingUser(TranquilModelEntity assessingUser) {
    this.assessingUser = assessingUser;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  public TranquilModelEntity getStudentProject() {
    return studentProject;
  }

  public void setStudentProject(TranquilModelEntity studentProject) {
    this.studentProject = studentProject;
  }

  private TranquilModelEntity grade;

  private TranquilModelEntity assessingUser;

  private TranquilModelEntity student;

  private TranquilModelEntity studentProject;

  public final static String[] properties = {"grade","assessingUser","student","studentProject"};
}
