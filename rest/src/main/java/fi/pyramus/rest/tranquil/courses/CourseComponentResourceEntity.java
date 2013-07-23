package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.CourseComponentResource.class, entityType = TranquilModelType.COMPACT)
public class CourseComponentResourceEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getUsagePercent() {
    return usagePercent;
  }

  public void setUsagePercent(Double usagePercent) {
    this.usagePercent = usagePercent;
  }

  public Long getCourseComponent_id() {
    return courseComponent_id;
  }

  public void setCourseComponent_id(Long courseComponent_id) {
    this.courseComponent_id = courseComponent_id;
  }

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

  private Long id;

  private Double usagePercent;

  private Long courseComponent_id;

  private Long resource_id;

  public final static String[] properties = {"id","usagePercent","courseComponent","resource"};
}
