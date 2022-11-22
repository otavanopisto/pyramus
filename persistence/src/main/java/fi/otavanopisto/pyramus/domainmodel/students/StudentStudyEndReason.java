package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;

@Entity
public class StudentStudyEndReason implements ArchivableEntity {

  /**
   * Returns internal unique id
   * 
   * @return Internal unique id
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
  
  protected void setParentReason(StudentStudyEndReason parentReason) {
    this.parentReason = parentReason;
  }

  public StudentStudyEndReason getParentReason() {
    return parentReason;
  }

  public void addChildEndReason(StudentStudyEndReason child) {
    if (!this.childEndReasons.contains(child)) {
      child.setParentReason(this);
      childEndReasons.add(child);
    } else {
      throw new PersistenceException("childEndReason is already in this StudentStudyEndReason");
    }
  }
  
  public void removeChildEndReason(StudentStudyEndReason child) {
    if (this.childEndReasons.contains(child)) {
      child.setParentReason(null);
      childEndReasons.remove(child);
    } else {
      throw new PersistenceException("childEndReason is not in this StudentStudyEndReason");
    }
  }

  public List<StudentStudyEndReason> getChildEndReasons() {
    return childEndReasons;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  @Override
  public Boolean getArchived() {
    return archived;
  }

  @Override
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentStudyEndReason")  
  @TableGenerator(name="StudentStudyEndReason", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;

  private String name;
  
  @ManyToOne
  @JoinColumn (name = "parentReason")
  private StudentStudyEndReason parentReason;

  @OneToMany
  @JoinColumn (name = "parentReason")
  private List<StudentStudyEndReason> childEndReasons = new ArrayList<>();

  @ElementCollection
  @MapKeyColumn (name = "name", length = 100)
  @Column (name = "value", length = 255)
  @CollectionTable (name = "StudentStudyEndReasonProperties", joinColumns = @JoinColumn(name = "studentStudyEndReason_id"))
  private Map<String, String> properties = new HashMap<String, String>();

  @Version
  @Column(nullable = false)
  private Long version;
  
  @NotNull
  @Column (nullable = false)
  private Boolean archived;
}
