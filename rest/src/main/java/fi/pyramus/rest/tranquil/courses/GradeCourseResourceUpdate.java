package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.GradeCourseResource.class, entityType = TranquilModelType.UPDATE)
public class GradeCourseResourceUpdate extends GradeCourseResourceComplete {

  public void setResource(ResourceCompact resource) {
    super.setResource(resource);
  }

  public ResourceCompact getResource() {
    return (ResourceCompact)super.getResource();
  }

  public void setCourse(CourseCompact course) {
    super.setCourse(course);
  }

  public CourseCompact getCourse() {
    return (CourseCompact)super.getCourse();
  }

  public final static String[] properties = {"resource","course"};
}
