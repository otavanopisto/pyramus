package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.projects.StudentProjectModule.class, entityType = TranquilModelType.UPDATE)
public class StudentProjectModuleUpdate extends StudentProjectModuleComplete {

  public void setModule(ModuleCompact module) {
    super.setModule(module);
  }

  public ModuleCompact getModule() {
    return (ModuleCompact)super.getModule();
  }

  public void setStudentProject(StudentProjectCompact studentProject) {
    super.setStudentProject(studentProject);
  }

  public StudentProjectCompact getStudentProject() {
    return (StudentProjectCompact)super.getStudentProject();
  }

  public void setAcademicTerm(AcademicTermCompact academicTerm) {
    super.setAcademicTerm(academicTerm);
  }

  public AcademicTermCompact getAcademicTerm() {
    return (AcademicTermCompact)super.getAcademicTerm();
  }

  public final static String[] properties = {"module","studentProject","academicTerm"};
}
