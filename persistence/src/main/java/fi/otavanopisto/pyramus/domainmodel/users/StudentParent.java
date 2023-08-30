package fi.otavanopisto.pyramus.domainmodel.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
  public boolean isAccountEnabled() {
    return true; // TODO ?
  }
  
  public List<Student> getChildren() {
    return children;
  }

  public void setChildren(List<Student> children) {
    this.children = children;
  }

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name = "StudentParentChildren", joinColumns = @JoinColumn(name = "studentParentId"), inverseJoinColumns = @JoinColumn(name = "studentId"))
  private List<Student> children = new ArrayList<>();
}
