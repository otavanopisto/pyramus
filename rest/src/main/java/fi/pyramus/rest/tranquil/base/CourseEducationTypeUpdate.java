package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseEducationType.class, entityType = TranquilModelType.UPDATE)
public class CourseEducationTypeUpdate extends CourseEducationTypeComplete {

  public void setCourseBase(CourseBaseCompact courseBase) {
    super.setCourseBase(courseBase);
  }

  public CourseBaseCompact getCourseBase() {
    return (CourseBaseCompact)super.getCourseBase();
  }

  public void setEducationType(EducationTypeCompact educationType) {
    super.setEducationType(educationType);
  }

  public EducationTypeCompact getEducationType() {
    return (EducationTypeCompact)super.getEducationType();
  }

  public void setCourseEducationSubtypes(java.util.List<CourseEducationSubtypeCompact> courseEducationSubtypes) {
    super.setCourseEducationSubtypes(courseEducationSubtypes);
  }

  public java.util.List<CourseEducationSubtypeCompact> getCourseEducationSubtypes() {
    return (java.util.List<CourseEducationSubtypeCompact>)super.getCourseEducationSubtypes();
  }

  public final static String[] properties = {"courseBase","educationType","courseEducationSubtypes"};
}
