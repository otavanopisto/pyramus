package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseEducationSubtype.class, entityType = TranquilModelType.UPDATE)
public class CourseEducationSubtypeUpdate extends CourseEducationSubtypeComplete {

  public void setCourseEducationType(CourseEducationTypeCompact courseEducationType) {
    super.setCourseEducationType(courseEducationType);
  }

  public CourseEducationTypeCompact getCourseEducationType() {
    return (CourseEducationTypeCompact)super.getCourseEducationType();
  }

  public void setEducationSubtype(EducationSubtypeCompact educationSubtype) {
    super.setEducationSubtype(educationSubtype);
  }

  public EducationSubtypeCompact getEducationSubtype() {
    return (EducationSubtypeCompact)super.getEducationSubtype();
  }

  public final static String[] properties = {"courseEducationType","educationSubtype"};
}
