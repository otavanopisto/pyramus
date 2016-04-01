package fi.otavanopisto.pyramus.rest.model;

public class StudentGroupUser {

  public StudentGroupUser() {
    super();
  }

  public StudentGroupUser(Long id, Long staffMemberId) {
    this();
    this.id = id;
    this.setStaffMemberId(staffMemberId);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  private Long id;
  private Long staffMemberId;
}
