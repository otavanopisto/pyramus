package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplate.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditTemplateEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public java.util.List<Long> getCourses_ids() {
    return courses_ids;
  }

  public void setCourses_ids(java.util.List<Long> courses_ids) {
    this.courses_ids = courses_ids;
  }

  private Long id;

  private String name;

  private Long version;

  private java.util.List<Long> courses_ids;

  public final static String[] properties = {"id","name","version","courses"};
}
