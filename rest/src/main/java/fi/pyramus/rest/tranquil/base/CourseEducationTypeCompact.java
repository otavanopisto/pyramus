package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.CourseEducationType.class, entityType = TranquilModelType.COMPACT)
public class CourseEducationTypeCompact extends CourseEducationTypeBase {

  public Long getCourseBase_id() {
    return courseBase_id;
  }

  public void setCourseBase_id(Long courseBase_id) {
    this.courseBase_id = courseBase_id;
  }

  public Long getEducationType_id() {
    return educationType_id;
  }

  public void setEducationType_id(Long educationType_id) {
    this.educationType_id = educationType_id;
  }

  public java.util.List<Long> getCourseEducationSubtypes_ids() {
    return courseEducationSubtypes_ids;
  }

  public void setCourseEducationSubtypes_ids(java.util.List<Long> courseEducationSubtypes_ids) {
    this.courseEducationSubtypes_ids = courseEducationSubtypes_ids;
  }

  private Long courseBase_id;

  private Long educationType_id;

  private java.util.List<Long> courseEducationSubtypes_ids;

  public final static String[] properties = {"courseBase","educationType","courseEducationSubtypes"};
}
