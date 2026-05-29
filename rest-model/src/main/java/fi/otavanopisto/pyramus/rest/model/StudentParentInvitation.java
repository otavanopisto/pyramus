package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

import fi.otavanopisto.security.ContextReference;

public class StudentParentInvitation implements ContextReference {

  public StudentParentInvitation() {
    super();
  }

  public StudentParentInvitation(Long id, String firstName, String lastName, String email, boolean continuedViewPermission, OffsetDateTime continuedViewPermissionModified) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.continuedViewPermission = continuedViewPermission;
    this.continuedViewPermissionModified = continuedViewPermissionModified;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isContinuedViewPermission() {
    return continuedViewPermission;
  }

  public void setContinuedViewPermission(boolean continuedViewPermission) {
    this.continuedViewPermission = continuedViewPermission;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public OffsetDateTime getContinuedViewPermissionModified() {
    return continuedViewPermissionModified;
  }

  public void setContinuedViewPermissionModified(OffsetDateTime continuedViewPermissionModified) {
    this.continuedViewPermissionModified = continuedViewPermissionModified;
  }

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private boolean continuedViewPermission;
  private OffsetDateTime continuedViewPermissionModified;
}
