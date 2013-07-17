package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.CourseEducationType.class, entityType = TranquilModelType.COMPLETE)
public class CourseEducationTypeComplete extends CourseEducationTypeBase {

  public TranquilModelEntity getCourseBase() {
    return courseBase;
  }

  public void setCourseBase(TranquilModelEntity courseBase) {
    this.courseBase = courseBase;
  }

  public TranquilModelEntity getEducationType() {
    return educationType;
  }

  public void setEducationType(TranquilModelEntity educationType) {
    this.educationType = educationType;
  }

  public java.util.List<TranquilModelEntity> getCourseEducationSubtypes() {
    return courseEducationSubtypes;
  }

  public void setCourseEducationSubtypes(java.util.List<TranquilModelEntity> courseEducationSubtypes) {
    this.courseEducationSubtypes = courseEducationSubtypes;
  }

  private TranquilModelEntity courseBase;

  private TranquilModelEntity educationType;

  private java.util.List<TranquilModelEntity> courseEducationSubtypes;

  public final static String[] properties = {"courseBase","educationType","courseEducationSubtypes"};
}
