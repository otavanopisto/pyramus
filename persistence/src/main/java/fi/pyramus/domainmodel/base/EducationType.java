package fi.pyramus.domainmodel.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
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
   * Returns the subtypes of this education type.
   * 
   * @return The subtypes of this education type
   */
  public List<EducationSubtype> getSubtypes() {
    return subtypes;
  }

  @Transient
  public List<EducationSubtype> getUnarchivedSubtypes() {
    List<EducationSubtype> subtypes = new ArrayList<EducationSubtype>();
    for (EducationSubtype subtype : this.subtypes) {
      if (!subtype.getArchived()) {
        subtypes.add(subtype);
      }
    }
    return subtypes;
  }

  @Transient
  public List<EducationSubtype> getArchivedSubtypes() {
    List<EducationSubtype> subtypes = new ArrayList<EducationSubtype>();
    for (EducationSubtype subtype : this.subtypes) {
      if (subtype.getArchived()) {
        subtypes.add(subtype);
      }
    }
    return subtypes;
  }

  /**
   * Sets the subtypes of this education type.
   * 
   * @param subtypes The subtypes of this education type
   */
  @SuppressWarnings("unused")
  private void setSubtypes(List<EducationSubtype> subtypes) {
    this.subtypes = subtypes;
  }

  /**
   * Adds a subtype to this education type.
   * 
   * @param subtype The subtype to be added
   */
  public void addSubtype(EducationSubtype subtype) {
    if (subtype.getEducationType() != null) {
      subtype.getEducationType().getSubtypes().remove(subtype);
    }
    subtype.setEducationType(this);
    this.subtypes.add(subtype);
  }
  
  /**
   * Removes the given subtype from this education type.
   * 
   * @param subtype The subtype to be removed
   */
  public void removeSubtype(EducationSubtype subtype) {
    subtype.setEducationType(null);
    this.subtypes.remove(subtype);
  }
  
  /**
   * Returns an education subtype corresponding to the given identifier. If such subtype cannot
   * be found, returns <code>null</code>.
   * 
   * @param educationSubtypeId Education subtype identifeir
   * 
   * @return An education subtype corresponding to the given identifier, or <code>null</code> if not found
   */
  @Transient
  public EducationSubtype getEducationSubtypeById(Long educationSubtypeId) {
    for (EducationSubtype educationSubtype : subtypes) {
      if (educationSubtype.getId().equals(educationSubtypeId)) {
        return educationSubtype;
      }
    }
    return null;
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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="educationType")
  @OrderBy("name")
  private List<EducationSubtype> subtypes = new Vector<EducationSubtype>();
  
  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
}
