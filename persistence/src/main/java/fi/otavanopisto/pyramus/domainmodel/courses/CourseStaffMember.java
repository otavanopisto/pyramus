package fi.otavanopisto.pyramus.domainmodel.courses;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.IndexedEmbedded;

import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

/**
 * Representation staff member within a course. For example, the teachers and tutors of a course. 
 */
@Entity
public class CourseStaffMember extends CourseUser {
  
  public CourseStaffMemberRole getRole() {
    return role;
  }
  
  public void setRole(CourseStaffMemberRole role) {
    this.role = role;
  }
  
  public StaffMember getStaffMember() {
    return staffMember;
  }

  public void setStaffMember(StaffMember staffMember) {
    this.staffMember = staffMember;
  }

  @ManyToOne 
  @IndexedEmbedded
  private StaffMember staffMember;
  
  @ManyToOne
  private CourseStaffMemberRole role;
}
