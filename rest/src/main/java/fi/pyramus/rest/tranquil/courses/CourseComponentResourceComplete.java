package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseComponentResource.class, entityType = TranquilModelType.COMPLETE)
public class CourseComponentResourceComplete extends CourseComponentResourceBase {

  public TranquilModelEntity getCourseComponent() {
    return courseComponent;
  }

  public void setCourseComponent(TranquilModelEntity courseComponent) {
    this.courseComponent = courseComponent;
  }

  public TranquilModelEntity getResource() {
    return resource;
  }

  public void setResource(TranquilModelEntity resource) {
    this.resource = resource;
  }

  private TranquilModelEntity courseComponent;

  private TranquilModelEntity resource;

  public final static String[] properties = {"courseComponent","resource"};
}
