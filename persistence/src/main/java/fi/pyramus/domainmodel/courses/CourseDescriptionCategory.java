package fi.pyramus.domainmodel.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import fi.pyramus.domainmodel.base.ArchivableEntity;

@Entity
public class CourseDescriptionCategory implements ArchivableEntity {

  /**
   * Returns the identifier of this entity.
   * 
   * @return The identifier of this entity
   */
  public Long getId() {
    return id;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
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

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseDescriptionCategory")  
  @TableGenerator(name="CourseDescriptionCategory", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String name;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;
}
