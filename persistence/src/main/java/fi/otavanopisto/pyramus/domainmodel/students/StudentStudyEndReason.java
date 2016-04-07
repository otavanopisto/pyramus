package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import org.hibernate.search.annotations.DocumentId;

@Entity
public class StudentStudyEndReason {

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

  @Version
  @Column(nullable = false)
  private Long version;
}
