package fi.pyramus.rest.model;

import java.util.List;
import java.util.Map;

public class StaffMember {

  public StaffMember() {
    super();
  }

  public StaffMember(Long id, String additionalContactInfo, String firstName, String lastName, String title, UserRole role, List<String> tags, Map<String, String> variables) {
    super();
    this.id = id;
    this.additionalContactInfo = additionalContactInfo;
    this.firstName = firstName;
    this.lastName = lastName;
    this.title = title;
    this.role = role;
    this.tags = tags;
    this.variables = variables;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAdditionalContactInfo() {
    return additionalContactInfo;
  }

  public void setAdditionalContactInfo(String additionalContactInfo) {
    this.additionalContactInfo = additionalContactInfo;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
  
  public List<String> getTags() {
    return tags;
  }
  
  public void setTags(List<String> tags) {
    this.tags = tags;
  }
  
  public Map<String, String> getVariables() {
    return variables;
  }
  
  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }

  private Long id;
  private String additionalContactInfo;
  private String firstName;
  private String lastName;
  private String title;
  private UserRole role;
  private Map<String, String> variables;
  private List<String> tags;
}
