package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.BasicCourseResource.class, entityType = TranquilModelType.COMPACT)
public class BasicCourseResourceEntity implements fi.tranquil.TranquilModelEntity {

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

  public Integer getUnits() {
    return units;
  }

  public void setUnits(Integer units) {
    this.units = units;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

  public Long getCourse_id() {
    return course_id;
  }

  public void setCourse_id(Long course_id) {
    this.course_id = course_id;
  }

  private Long id;

  private Double hours;

  private fi.pyramus.persistence.usertypes.MonetaryAmount hourlyCost;

  private fi.pyramus.persistence.usertypes.MonetaryAmount unitCost;

  private Integer units;

  private Long version;

  private Long resource_id;

  private Long course_id;

  public final static String[] properties = {"id","hours","hourlyCost","unitCost","units","version","resource","course"};
}
