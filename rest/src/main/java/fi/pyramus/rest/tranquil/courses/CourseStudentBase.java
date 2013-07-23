package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseStudent.class, entityType = TranquilModelType.BASE)
public class CourseStudentBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getEnrolmentTime() {
    return enrolmentTime;
  }

  public void setEnrolmentTime(java.util.Date enrolmentTime) {
    this.enrolmentTime = enrolmentTime;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getLodging() {
    return lodging;
  }

  public void setLodging(Boolean lodging) {
    this.lodging = lodging;
  }

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private java.util.Date enrolmentTime;

  private Boolean archived;

  private Boolean lodging;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long version;

  public final static String[] properties = {"id","enrolmentTime","archived","lodging","optionality","version"};
}
