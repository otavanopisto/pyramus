package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.base.AcademicTerm.class, entityType = TranquilModelType.BASE)
public class AcademicTermBase implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getStartDate() {
    return startDate;
  }

  public void setStartDate(java.util.Date startDate) {
    this.startDate = startDate;
  }

  public java.util.Date getEndDate() {
    return endDate;
  }

  public void setEndDate(java.util.Date endDate) {
    this.endDate = endDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private java.util.Date startDate;

  private java.util.Date endDate;

  private String name;

  private Boolean archived;

  private Long version;

  public final static String[] properties = {"id","startDate","endDate","name","archived","version"};
}
