package fi.pyramus.rest.tranquil.reports;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.reports.ReportCategory.class, entityType = TranquilModelType.COMPACT)
public class ReportCategoryEntity implements fi.tranquil.TranquilModelEntity {

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

  public Integer getIndexColumn() {
    return indexColumn;
  }

  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  private Long id;

  private String name;

  private Integer indexColumn;

  private Long version;

  public final static String[] properties = {"id","name","indexColumn","version"};
}
