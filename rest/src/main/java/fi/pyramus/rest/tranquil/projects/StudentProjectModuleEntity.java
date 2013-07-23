package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.StudentProjectModule.class, entityType = TranquilModelType.COMPACT)
public class StudentProjectModuleEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

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

  private Long id;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long version;

  private Long module_id;

  private Long studentProject_id;

  private Long academicTerm_id;

  public final static String[] properties = {"id","optionality","version","module","studentProject","academicTerm"};
}
