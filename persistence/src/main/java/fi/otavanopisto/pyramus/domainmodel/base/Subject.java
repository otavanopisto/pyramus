package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@FullTextFilterDefs (
    @FullTextFilterDef (
       name="ArchivedSubject",
       impl=ArchivedEntityFilterFactory.class
    )
  )
public class Subject implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  
  public String getName() {
    return name;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * Sets the archived flag of this object.
   * 
   * @param archived The archived flag of this object
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Returns the archived flag of this object.
   * 
   * @return The archived flag of this object
   */
  public Boolean getArchived() {
    return archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  public EducationType getEducationType() {
    return educationType;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Subject")  
  @TableGenerator(name="Subject", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @Field
  private String code;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field(store=Store.NO)
  private String name;
  
  @IndexedEmbedded
  @ManyToOne
  @JoinColumn(name="educationType")
  private EducationType educationType;
  
  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
