package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.BasicCourseResource.class, entityType = TranquilModelType.COMPACT)
public class BasicCourseResourceCompact extends BasicCourseResourceBase {

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  private Long resource_id;

  private Long course_id;

  public final static String[] properties = {"resource","course"};
}
