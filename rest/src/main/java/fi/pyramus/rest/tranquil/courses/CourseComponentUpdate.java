package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseComponent.class, entityType = TranquilModelType.UPDATE)
public class CourseComponentUpdate extends CourseComponentComplete {

  public void setLength(EducationalLengthCompact length) {
    super.setLength(length);
  }

  public EducationalLengthCompact getLength() {
    return (EducationalLengthCompact)super.getLength();
  }

  public void setCourse(CourseCompact course) {
    super.setCourse(course);
  }

  public CourseCompact getCourse() {
    return (CourseCompact)super.getCourse();
  }

  public void setResources(java.util.List<CourseComponentResourceCompact> resources) {
    super.setResources(resources);
  }

  public java.util.List<CourseComponentResourceCompact> getResources() {
    return (java.util.List<CourseComponentResourceCompact>)super.getResources();
  }

  public final static String[] properties = {"length","course","resources"};
}
