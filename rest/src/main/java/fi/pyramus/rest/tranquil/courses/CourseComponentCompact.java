package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseComponent.class, entityType = TranquilModelType.COMPACT)
public class CourseComponentCompact extends CourseComponentBase {

  public Long getLength_id() {
    return length_id;
  }

  public void setLength_id(Long length_id) {
    this.length_id = length_id;
  }

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  public java.util.List<Long> getResources_ids() {
    return resources_ids;
  }

  public void setResources_ids(java.util.List<Long> resources_ids) {
    this.resources_ids = resources_ids;
  }

  private Long length_id;

  private Long course_id;

  private java.util.List<Long> resources_ids;

  public final static String[] properties = {"length","course","resources"};
}
