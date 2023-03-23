package fi.otavanopisto.pyramus.domainmodel.courses;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.IndexedEmbedded;

import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

/**
 * Representation staff member within a course. For example, the teachers and tutors of a course. 
 */
@Entity
public class CourseStaffMember extends CourseUser {
  
  public StaffMember getStaffMember() {
    return staffMember;
  }

  public void setStaffMember(StaffMember staffMember) {
    this.staffMember = staffMember;
  }

  public CourseStaffMemberRoleEnum getRole() {
    return role;
  }

  public void setRole(CourseStaffMemberRoleEnum role) {
    this.role = role;
  }

  @ManyToOne 
  @IndexedEmbedded
  private StaffMember staffMember;
  
  @Enumerated(EnumType.STRING)
  private CourseStaffMemberRoleEnum role;
}
