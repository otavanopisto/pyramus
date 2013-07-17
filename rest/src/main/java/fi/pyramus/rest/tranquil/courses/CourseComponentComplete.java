package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseComponent.class, entityType = TranquilModelType.COMPLETE)
public class CourseComponentComplete extends CourseComponentBase {

  public TranquilModelEntity getLength() {
    return length;
  }

  public void setLength(TranquilModelEntity length) {
    this.length = length;
  }

  public TranquilModelEntity getCourse() {
    return course;
  }

  public void setCourse(TranquilModelEntity course) {
    this.course = course;
  }

  public java.util.List<TranquilModelEntity> getResources() {
    return resources;
  }

  public void setResources(java.util.List<TranquilModelEntity> resources) {
    this.resources = resources;
  }

  private TranquilModelEntity length;

  private TranquilModelEntity course;

  private java.util.List<TranquilModelEntity> resources;

  public final static String[] properties = {"length","course","resources"};
}
