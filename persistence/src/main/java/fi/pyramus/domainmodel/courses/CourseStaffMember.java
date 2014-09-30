package fi.pyramus.domainmodel.courses;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.IndexedEmbedded;

import fi.pyramus.domainmodel.users.User;

/**
 * Representation staff member within a course. For example, the teachers and tutors of a course. 
 */
@Entity
public class CourseStaffMember extends CourseUser {
  
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  @ManyToOne 
  @JoinColumn(name="pyramusUser")
  @IndexedEmbedded
  private User user;
}
