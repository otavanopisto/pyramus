package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.StudentProjectModule.class, entityType = TranquilModelType.COMPLETE)
public class StudentProjectModuleComplete extends StudentProjectModuleBase {

  public TranquilModelEntity getModule() {
    return module;
  }

  public void setModule(TranquilModelEntity module) {
    this.module = module;
  }

  public TranquilModelEntity getStudentProject() {
    return studentProject;
  }

  public void setStudentProject(TranquilModelEntity studentProject) {
    this.studentProject = studentProject;
  }

  public TranquilModelEntity getAcademicTerm() {
    return academicTerm;
  }

  public void setAcademicTerm(TranquilModelEntity academicTerm) {
    this.academicTerm = academicTerm;
  }

  private TranquilModelEntity module;

  private TranquilModelEntity studentProject;

  private TranquilModelEntity academicTerm;

  public final static String[] properties = {"module","studentProject","academicTerm"};
}
