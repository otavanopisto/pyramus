package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.Defaults.class, entityType = TranquilModelType.COMPACT)
public class DefaultsEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getInitialCourseState_id() {
    return initialCourseState_id;
  }

  public void setInitialCourseState_id(Long initialCourseState_id) {
    this.initialCourseState_id = initialCourseState_id;
  }

  public Long getBaseTimeUnit_id() {
    return baseTimeUnit_id;
  }

  public void setBaseTimeUnit_id(Long baseTimeUnit_id) {
    this.baseTimeUnit_id = baseTimeUnit_id;
  }

  public Long getInitialCourseParticipationType_id() {
    return initialCourseParticipationType_id;
  }

  public void setInitialCourseParticipationType_id(Long initialCourseParticipationType_id) {
    this.initialCourseParticipationType_id = initialCourseParticipationType_id;
  }

  public Long getInitialCourseEnrolmentType_id() {
    return initialCourseEnrolmentType_id;
  }

  public void setInitialCourseEnrolmentType_id(Long initialCourseEnrolmentType_id) {
    this.initialCourseEnrolmentType_id = initialCourseEnrolmentType_id;
  }

  private Long id;

  private Long version;

  private Long initialCourseState_id;

  private Long baseTimeUnit_id;

  private Long initialCourseParticipationType_id;

  private Long initialCourseEnrolmentType_id;

  public final static String[] properties = {"id","version","initialCourseState","baseTimeUnit","initialCourseParticipationType","initialCourseEnrolmentType"};
}
