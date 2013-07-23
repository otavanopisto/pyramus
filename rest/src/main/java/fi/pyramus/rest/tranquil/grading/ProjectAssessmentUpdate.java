package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.ProjectAssessment.class, entityType = TranquilModelType.UPDATE)
public class ProjectAssessmentUpdate extends ProjectAssessmentComplete {

  public void setGrade(GradeCompact grade) {
    super.setGrade(grade);
  }

  public GradeCompact getGrade() {
    return (GradeCompact)super.getGrade();
  }

  public void setAssessingUser(UserCompact assessingUser) {
    super.setAssessingUser(assessingUser);
  }

  public UserCompact getAssessingUser() {
    return (UserCompact)super.getAssessingUser();
  }

  public void setStudentProject(StudentProjectCompact studentProject) {
    super.setStudentProject(studentProject);
  }

  public StudentProjectCompact getStudentProject() {
    return (StudentProjectCompact)super.getStudentProject();
  }

  public final static String[] properties = {"grade","assessingUser","studentProject"};
}
