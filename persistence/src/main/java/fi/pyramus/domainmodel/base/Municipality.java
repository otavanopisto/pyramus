package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Municipality class.
 * 
 * Defines attributes for a municipality
 * Municipalities have the following attributes:
 * - internal unique id used for internal linking
 * - external id for context dependent referring
 * - name for user friendly listing
 * - deprecation value that can be used to filter outdated municipalities from listings 
 * 
 * @author antti.viljakainen
 */

@Entity
@Indexed
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Municipality implements ArchivableEntity {

  /**
   * Returns the unique identifier of this municipality.
   * 
   * @return The unique identifier of this municipality
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Sets municipality id that identifies the municipality in a context.
   * 
   * @param code New municipality id
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * Returns the municipality code given for this instance.
   * 
   * @return The municipality code given for this instance
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets name of the municipality this instance represents.
   * 
   * @param name New municipality name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the municipality name given for this instance.
   * 
   * @return municipality name
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Municipality")  
  @TableGenerator(name="Municipality", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId 
  private Long id;

  @NotNull
  @NotEmpty
  @Pattern(regexp="^\\d+$")
  @Column (nullable = false)
  @Field
  private String code;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @Field (store = Store.YES)
  private String name;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
