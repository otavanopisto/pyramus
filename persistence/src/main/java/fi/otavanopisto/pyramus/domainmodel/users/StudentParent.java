package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
@Cache (usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@PrimaryKeyJoinColumn(name="id")
public class StudentParent extends User {

  @Override
  public Set<Role> getRoles() {
    return Set.of(Role.STUDENT_PARENT);
  }
  
  @Override
  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  /**
   * StudentParent's account is enabled if any single one of the children is underage
   * or otherwise able to still be accessed. If none of the children are such, the account
   * is automatically disabled.
   */
  @Override
  public boolean isAccountEnabled() {
    List<StudentParentChild> currentChildren = getChildren();
    
    for (StudentParentChild parentChild : currentChildren) {
      Student child = parentChild.getStudent();
      
      // Account is enabled if any of the students are active
      if (isActiveChild(child)) {
        return true;
      }
    }
    
    return false;
  }
  
  public List<StudentParentChild> getChildren() {
    return children;
  }

  @Transient
  public List<StudentParentChild> getActiveChildren() {
    List<StudentParentChild> childs = getChildren();
    return CollectionUtils.isNotEmpty(childs)
        ? childs.stream().filter(child -> isActiveChild(child.getStudent())).collect(Collectors.toList())
        : Collections.emptyList();
  }

  /**
   * Returns true if this StudentParent is active parent of given Student.
   * StudentParent is active if the student is underage or otherwise has
   * extended permissions to act as student's parent.
   * 
   * @param student Student
   * @return true if this StudentParent is active parent of given Student
   */
  @Transient
  public boolean isActiveParentOf(Student student) {
    if (student == null) {
      throw new IllegalArgumentException();
    }
    
    List<StudentParentChild> studentParentChildren = getChildren();
    for (StudentParentChild studentParentChild : studentParentChildren) {
      if (studentParentChild != null && studentParentChild.getStudent() != null && Boolean.FALSE.equals(studentParentChild.getStudent().getArchived())) {
        if (studentParentChild.getStudent().getId().equals(student.getId())) {
          // Student is in the list, check that the time/age restriction holds
          return isActiveChild(studentParentChild.getStudent());
        }
      }
    }

    return false;
  }
  
  /**
   * Returns true if given Student is underage. This is meant for use in
   * StudentParent context only because potentially missing birthday for 
   * Student produces a third state that cannot be expressed with a boolean.
   * That state in other contexts is likely important though.
   * 
   * @param student
   * @return
   */
  @Transient
  private boolean isActiveChild(Student student) {
    // Archived student or student who is not active is never active
    if (student.getArchived() || !student.getActive()) {
      return false;
    }
    
    // Check for underage
    return student.getPerson() != null && Boolean.TRUE.equals(student.getPerson().isUnderAge());
  }
  
  @OneToMany (mappedBy = "studentParent")
  private List<StudentParentChild> children;

  @ManyToOne
  @JoinColumn (name = "organization")
  private Organization organization;
  
}
