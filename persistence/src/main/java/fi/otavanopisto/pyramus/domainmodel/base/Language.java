package fi.otavanopisto.pyramus.domainmodel.base;

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
 * Language
 *  
 * @author antti.viljakainen
 */

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Language implements ArchivableEntity {

  /**
   * Returns internal unique id.
   * 
   * @return internal unique id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the language code of this language.
   * 
   * @param code The language code
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Returns the language code of this language.
   * 
   * @return The language code of this language
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets user friendly name of this language.
   * 
   * @param name User friendly name of this language
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns user friendly name of this language.
   * 
   * @return User friendly name of this language
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Language")  
  @TableGenerator(name="Language", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String code;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;  

  @Version
  @Column(nullable = false)
  private Long version;
}
