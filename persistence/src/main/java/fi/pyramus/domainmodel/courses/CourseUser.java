package fi.pyramus.domainmodel.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import org.hibernate.search.annotations.IndexedEmbedded;

import fi.pyramus.domainmodel.users.User;

/**
 * Representation of a Pyramus user within a course. Models, for example, the teachers and tutors of a course. 
 */
@Entity
public class CourseUser {
  
  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public void setRole(CourseUserRole role) {
    this.role = role;
  }

  public CourseUserRole getRole() {
    return role;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseUser")  
  @TableGenerator(name="CourseUser", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name="course")
  private Course course;
  
  @ManyToOne 
  @JoinColumn(name="pyramusUser")
  @IndexedEmbedded
  private User user;
  
  @ManyToOne 
  @JoinColumn(name="userRole")
  @IndexedEmbedded
  private CourseUserRole role;

  @Version
  @Column(nullable = false)
  private Long version;
}
