package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Education type, e.g. polytechnic, university, or adult education.
 * 
 * @author Pasi Kukkonen
 */
@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class EducationType implements ArchivableEntity {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the name of this education type.
   * 
   * @return The name of this education type
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this education type.
   * 
   * @param name The name of this education type
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Returns the code of this education type
   * 
   * @return code of this education type
   */
  public String getCode() {
    return code;
  }
  
  /**
   * Sets the code of this education type.
   * 
   * @param name the code of this education type
   */
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

  @Id
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="EducationType")  
  @TableGenerator(name="EducationType", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  @Field
  private String name;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String code;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
