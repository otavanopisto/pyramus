package fi.otavanopisto.pyramus.domainmodel.base;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Education subtype to further define the education type it belongs to.
 */
@Entity
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class EducationSubtype implements ArchivableEntity {
  
  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the name of this subtype.
   * 
   * @return The name of this subtype
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this subtype.
   * 
   * @param name The name of this sutype
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Returns the code of this education subtype
   * 
   * @return code of this education subtype
   */
  public String getCode() {
    return code;
  }
  
  /**
   * Sets the code of this education subtype.
   * 
   * @param name the code of this education subtype
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * Returns the education type of this subtype.
   * 
   * @return The education type of this subtype
   */
  public EducationType getEducationType() {
    return educationType;
  }
  
  /**
   * Sets the education type of this subtype.
   * 
   * @param educationType The education type of this subtype
   */
  public void setEducationType(EducationType educationType) {
    this.educationType = educationType;
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

  @Id
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="EducationSubtype")  
  @TableGenerator(name="EducationSubtype", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String name;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @KeywordField
  private String code;

  @ManyToOne
  @JoinColumn(name="educationType")
  private EducationType educationType;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
