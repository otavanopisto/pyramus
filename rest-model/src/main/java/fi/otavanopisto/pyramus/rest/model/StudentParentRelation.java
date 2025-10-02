package fi.otavanopisto.pyramus.rest.model;

/**
 * Rest-model for Student - StudentParent (aka StudentParentChild with extra steps) relation.
 */
public class StudentParentRelation {

  public StudentParentRelation() {
  }

  public StudentParentRelation(Long id, String firstName, String lastName, boolean continuedViewPermission) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.continuedViewPermission = continuedViewPermission;
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

  private Long id;
  private boolean continuedViewPermission;
  private String firstName;
  private String lastName;
}
