package fi.otavanopisto.pyramus.domainmodel.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

/**
 * Nationality
 * 
 * @author antti.viljakainen
 */

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Nationality implements ArchivableEntity {

  /**
   * Returns internal unique id.
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets context dependent nationality code for this object.
   *  
   * @param code Nationality code in a context
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Returns context dependent nationality code.
   * 
   * @return Nationality code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets user friendly name for this nationality.
   * 
   * @param name User friendly name for this nationality
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns user friendly name of this nationality.
   * 
   * @return User friendly name of this nationality
   */
  public String getName() {
    return name;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Nationality")  
  @TableGenerator(name="Nationality", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @KeywordField
  private String code;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @FullTextField
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
