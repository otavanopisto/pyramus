package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseComponentResource.class, entityType = TranquilModelType.UPDATE)
public class CourseComponentResourceUpdate extends CourseComponentResourceComplete {

  public void setCourseComponent(CourseComponentCompact courseComponent) {
    super.setCourseComponent(courseComponent);
  }

  public CourseComponentCompact getCourseComponent() {
    return (CourseComponentCompact)super.getCourseComponent();
  }

  public void setResource(ResourceCompact resource) {
    super.setResource(resource);
  }

  public ResourceCompact getResource() {
    return (ResourceCompact)super.getResource();
  }

  public final static String[] properties = {"courseComponent","resource"};
}
