package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.StudentProject.class, entityType = TranquilModelType.COMPLETE)
public class StudentProjectComplete extends StudentProjectBase {

  public TranquilModelEntity getCreator() {
    return creator;
  }

  public void setCreator(TranquilModelEntity creator) {
    this.creator = creator;
  }

  public TranquilModelEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(TranquilModelEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  public TranquilModelEntity getOptionalStudiesLength() {
    return optionalStudiesLength;
  }

  public void setOptionalStudiesLength(TranquilModelEntity optionalStudiesLength) {
    this.optionalStudiesLength = optionalStudiesLength;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  public TranquilModelEntity getProject() {
    return project;
  }

  public void setProject(TranquilModelEntity project) {
    this.project = project;
  }

  public java.util.List<TranquilModelEntity> getStudentProjectModules() {
    return studentProjectModules;
  }

  public void setStudentProjectModules(java.util.List<TranquilModelEntity> studentProjectModules) {
    this.studentProjectModules = studentProjectModules;
  }

  private TranquilModelEntity creator;

  private TranquilModelEntity lastModifier;

  private TranquilModelEntity optionalStudiesLength;

  private TranquilModelEntity student;

  private TranquilModelEntity project;

  private java.util.List<TranquilModelEntity> studentProjectModules;

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","student","project","studentProjectModules"};
}
