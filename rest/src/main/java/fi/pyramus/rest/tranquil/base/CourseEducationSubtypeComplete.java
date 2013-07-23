package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseEducationSubtype.class, entityType = TranquilModelType.COMPLETE)
public class CourseEducationSubtypeComplete extends CourseEducationSubtypeBase {

  public TranquilModelEntity getCourseEducationType() {
    return courseEducationType;
  }

  public void setCourseEducationType(TranquilModelEntity courseEducationType) {
    this.courseEducationType = courseEducationType;
  }

  public TranquilModelEntity getEducationSubtype() {
    return educationSubtype;
  }

  public void setEducationSubtype(TranquilModelEntity educationSubtype) {
    this.educationSubtype = educationSubtype;
  }

  private TranquilModelEntity courseEducationType;

  private TranquilModelEntity educationSubtype;

  public final static String[] properties = {"courseEducationType","educationSubtype"};
}
