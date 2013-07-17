package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseComponentResource.class, entityType = TranquilModelType.BASE)
public class CourseComponentResourceBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getUsagePercent() {
    return usagePercent;
  }

  public void setUsagePercent(Double usagePercent) {
    this.usagePercent = usagePercent;
  }

  private Long id;

  private Double usagePercent;

  public final static String[] properties = {"id","usagePercent"};
}
