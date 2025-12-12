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

import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
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
    List<StudentParentChild> childs = getChildren();
    return CollectionUtils.isNotEmpty(childs) && childs.stream().anyMatch(child -> isActiveChild(child));
  }
  
  public List<StudentParentChild> getChildren() {
    return children;
  }

  @Transient
  public List<StudentParentChild> getActiveChildren() {
    List<StudentParentChild> childs = getChildren();
    return CollectionUtils.isNotEmpty(childs)
        ? childs.stream().filter(child -> isActiveChild(child)).collect(Collectors.toList())
        : Collections.emptyList();
  }

  /**
   * Returns true if this StudentParent is a parent of given Student.
   * 
   * @param student Student
   * @return true if this StudentParent is a parent of given Student
   */
  @Transient
  public boolean isParentOf(Student student) {
    if (student == null) {
      throw new IllegalArgumentException();
    }
    
    return findStudentParentChild(student) != null;
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
    
    StudentParentChild studentParentChild = findStudentParentChild(student);
    return studentParentChild != null && isActiveChild(studentParentChild);
  }

  /**
   * Returns StudentParentChild for a Student if such exists.
   * 
   * @param student student
   * @return StudentParentChild for the student or null if student is not in the list
   */
  @Transient
  private StudentParentChild findStudentParentChild(Student student) {
    List<StudentParentChild> studentParentChildren = getChildren();
    for (StudentParentChild studentParentChild : studentParentChildren) {
      if (studentParentChild != null && studentParentChild.getStudent() != null && Boolean.FALSE.equals(studentParentChild.getStudent().getArchived())) {
        if (studentParentChild.getStudent().getId().equals(student.getId())) {
          return studentParentChild;
        }
      }
    }
    
    return null;
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
  private boolean isActiveChild(StudentParentChild studentParentChild) {
    Student student = studentParentChild.getStudent();
    
    // Archived student or student who is not active is never active
    if (student.getArchived() || !student.getActive()) {
      return false;
    }
    
    // Check that the student is either underage (implicit permission) or that the continued permission is granted
    return student.getPerson() != null && (Boolean.TRUE.equals(student.getPerson().isUnderAge()) || studentParentChild.isContinuedViewPermission());
  }
  
  @OneToMany (mappedBy = "studentParent")
  private List<StudentParentChild> children;

  @ManyToOne
  @JoinColumn (name = "organization")
  private Organization organization;
  
}
