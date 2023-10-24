package fi.otavanopisto.pyramus.domainmodel.reports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedReportCategory",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class ReportCategory implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setIndexColumn(Integer indexColumn) {
    this.indexColumn = indexColumn;
  }

  public Integer getIndexColumn() {
    return indexColumn;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }
  
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ReportCategory")  
  @TableGenerator(name="ReportCategory", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String name;

  private Integer indexColumn;

  @Version
  @Column(nullable = false)
  private Long version;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;
}
