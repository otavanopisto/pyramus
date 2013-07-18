package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.StudentCourseResource.class, entityType = TranquilModelType.BASE)
public class StudentCourseResourceBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getHours() {
    return hours;
  }

  public void setHours(Double hours) {
    this.hours = hours;
  }

  public fi.pyramus.persistence.usertypes.MonetaryAmount getHourlyCost() {
    return hourlyCost;
  }

  public void setHourlyCost(fi.pyramus.persistence.usertypes.MonetaryAmount hourlyCost) {
    this.hourlyCost = hourlyCost;
  }

  public fi.pyramus.persistence.usertypes.MonetaryAmount getUnitCost() {
    return unitCost;
  }

  public void setUnitCost(fi.pyramus.persistence.usertypes.MonetaryAmount unitCost) {
    this.unitCost = unitCost;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private Double hours;

  private fi.pyramus.persistence.usertypes.MonetaryAmount hourlyCost;

  private fi.pyramus.persistence.usertypes.MonetaryAmount unitCost;

  private Long version;

  public final static String[] properties = {"id","hours","hourlyCost","unitCost","version"};
}
