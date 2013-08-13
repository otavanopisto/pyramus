package fi.pyramus.domainmodel.reports;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedReport",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class Report implements ArchivableEntity{

  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getData() {
    return data;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public Date getCreated() {
    return created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public User getCreator() {
    return creator;
  }
  
  public void setCreator(User creator) {
    this.creator = creator;
  }
  
  public Date getLastModified() {
    return lastModified;
  }
  
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }
  
  public User getLastModifier() {
    return lastModifier;
  }
  
  public void setLastModifier(User lastModifier) {
    this.lastModifier = lastModifier;
  }
  
  public void setCategory(ReportCategory category) {
    this.category = category;
  }

  public ReportCategory getCategory() {
    return category;
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Report")  
  @TableGenerator(name="Report", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "category")
  private ReportCategory category;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String name;

  @Basic (fetch = FetchType.LAZY)
  @Column (nullable = false, length=1073741824)
  @NotEmpty
  private String data;
  
  @ManyToOne 
  @JoinColumn(name="creator")
  private User creator;
  
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;
  
  @ManyToOne  
  @JoinColumn(name="lastModifier")
  private User lastModifier;
  
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date lastModified;

  @Version
  @Column(nullable = false)
  private Long version;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;
}
