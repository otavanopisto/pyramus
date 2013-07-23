package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.CourseEducationSubtype.class, entityType = TranquilModelType.COMPACT)
public class CourseEducationSubtypeEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getCourseEducationType_id() {
    return courseEducationType_id;
  }

  public void setCourseEducationType_id(Long courseEducationType_id) {
    this.courseEducationType_id = courseEducationType_id;
  }

  public Long getEducationSubtype_id() {
    return educationSubtype_id;
  }

  public void setEducationSubtype_id(Long educationSubtype_id) {
    this.educationSubtype_id = educationSubtype_id;
  }

  private Long id;

  private Long version;

  private Long courseEducationType_id;

  private Long educationSubtype_id;

  public final static String[] properties = {"id","version","courseEducationType","educationSubtype"};
}
