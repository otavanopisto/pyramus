package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.StudentProjectModule.class, entityType = TranquilModelType.COMPACT)
public class StudentProjectModuleCompact extends StudentProjectModuleBase {

  public Long getModule_id() {
    return module_id;
  }

  public void setModule_id(Long module_id) {
    this.module_id = module_id;
  }

  public Long getStudentProject_id() {
    return studentProject_id;
  }

  public void setStudentProject_id(Long studentProject_id) {
    this.studentProject_id = studentProject_id;
  }

  public Long getAcademicTerm_id() {
    return academicTerm_id;
  }

  public void setAcademicTerm_id(Long academicTerm_id) {
    this.academicTerm_id = academicTerm_id;
  }

  private Long module_id;

  private Long studentProject_id;

  private Long academicTerm_id;

  public final static String[] properties = {"module","studentProject","academicTerm"};
}
