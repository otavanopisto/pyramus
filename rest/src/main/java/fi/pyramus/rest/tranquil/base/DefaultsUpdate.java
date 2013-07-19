package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Defaults.class, entityType = TranquilModelType.UPDATE)
public class DefaultsUpdate extends DefaultsComplete {

  public void setInitialCourseState(CourseStateCompact initialCourseState) {
    super.setInitialCourseState(initialCourseState);
  }

  public CourseStateCompact getInitialCourseState() {
    return (CourseStateCompact)super.getInitialCourseState();
  }

  public void setBaseTimeUnit(EducationalTimeUnitCompact baseTimeUnit) {
    super.setBaseTimeUnit(baseTimeUnit);
  }

  public EducationalTimeUnitCompact getBaseTimeUnit() {
    return (EducationalTimeUnitCompact)super.getBaseTimeUnit();
  }

  public void setInitialCourseParticipationType(CourseParticipationTypeCompact initialCourseParticipationType) {
    super.setInitialCourseParticipationType(initialCourseParticipationType);
  }

  public CourseParticipationTypeCompact getInitialCourseParticipationType() {
    return (CourseParticipationTypeCompact)super.getInitialCourseParticipationType();
  }

  public void setInitialCourseEnrolmentType(CourseEnrolmentTypeCompact initialCourseEnrolmentType) {
    super.setInitialCourseEnrolmentType(initialCourseEnrolmentType);
  }

  public CourseEnrolmentTypeCompact getInitialCourseEnrolmentType() {
    return (CourseEnrolmentTypeCompact)super.getInitialCourseEnrolmentType();
  }

  public final static String[] properties = {"initialCourseState","baseTimeUnit","initialCourseParticipationType","initialCourseEnrolmentType"};
}
