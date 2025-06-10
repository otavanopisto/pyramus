package fi.otavanopisto.pyramus.domainmodel.base;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class SchoolField implements ArchivableEntity {

  /**
   * Returns internal unique id.
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }
  
  /**
   * Sets user friendly name for this school field
   * @param name new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns user friendly name for this school field
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets archived status for this object
   * 
   * @param archived
   */
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  /**
   * Sets archived status for this object
   * @return
   */
  public Boolean getArchived() {
    return archived;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="SchoolField")  
  @TableGenerator(name="SchoolField", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @NotNull
  @NotEmpty
  @Column (nullable = false)
  @FullTextField
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived = Boolean.FALSE;
}
