package fi.otavanopisto.pyramus.domainmodel.users;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
      
      if (child.getPerson() != null && child.getPerson().getBirthday() != null) {
        LocalDate birthday = Instant.ofEpochMilli(child.getPerson().getBirthday().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate threshold = LocalDate.now().minusYears(18);

        if (birthday.isAfter(threshold)) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  public List<StudentParentChild> getChildren() {
    return children;
  }

  @OneToMany (mappedBy = "studentParent")
  private List<StudentParentChild> children;
  
}
