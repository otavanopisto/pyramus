package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.BasicCourseResource.class, entityType = TranquilModelType.COMPLETE)
public class BasicCourseResourceComplete extends BasicCourseResourceBase {

  public TranquilModelEntity getResource() {
    return resource;
  }

  public void setResource(TranquilModelEntity resource) {
    this.resource = resource;
  }

  public TranquilModelEntity getCourse() {
    return course;
  }

  public void setCourse(TranquilModelEntity course) {
    this.course = course;
  }

  private TranquilModelEntity resource;

  private TranquilModelEntity course;

  public final static String[] properties = {"resource","course"};
}
