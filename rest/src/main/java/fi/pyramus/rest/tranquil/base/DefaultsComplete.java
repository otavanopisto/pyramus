package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.Defaults.class, entityType = TranquilModelType.COMPLETE)
public class DefaultsComplete extends DefaultsBase {

  public TranquilModelEntity getInitialCourseState() {
    return initialCourseState;
  }

  public void setInitialCourseState(TranquilModelEntity initialCourseState) {
    this.initialCourseState = initialCourseState;
  }

  public TranquilModelEntity getBaseTimeUnit() {
    return baseTimeUnit;
  }

  public void setBaseTimeUnit(TranquilModelEntity baseTimeUnit) {
    this.baseTimeUnit = baseTimeUnit;
  }

  public TranquilModelEntity getInitialCourseParticipationType() {
    return initialCourseParticipationType;
  }

  public void setInitialCourseParticipationType(TranquilModelEntity initialCourseParticipationType) {
    this.initialCourseParticipationType = initialCourseParticipationType;
  }

  public TranquilModelEntity getInitialCourseEnrolmentType() {
    return initialCourseEnrolmentType;
  }

  public void setInitialCourseEnrolmentType(TranquilModelEntity initialCourseEnrolmentType) {
    this.initialCourseEnrolmentType = initialCourseEnrolmentType;
  }

  private TranquilModelEntity initialCourseState;

  private TranquilModelEntity baseTimeUnit;

  private TranquilModelEntity initialCourseParticipationType;

  private TranquilModelEntity initialCourseEnrolmentType;

  public final static String[] properties = {"initialCourseState","baseTimeUnit","initialCourseParticipationType","initialCourseEnrolmentType"};
}
