package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;

import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;

@Entity
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedSchoolField",
     impl=ArchivedEntityFilterFactory.class
  )
)
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
  @Field
  private String name;
  
  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;
}
