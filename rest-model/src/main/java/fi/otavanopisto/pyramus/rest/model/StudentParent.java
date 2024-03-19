package fi.otavanopisto.pyramus.rest.model;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import fi.otavanopisto.security.ContextReference;

public class StudentParent implements ContextReference {

  public StudentParent() {
    super();
  }

  public StudentParent(Long id, Long personId, Long organizationId, String additionalContactInfo, String firstName, String lastName, EnumSet<UserRole> roles,
      List<String> tags, Map<String, String> variables) {
    super();
    this.id = id;
    this.personId = personId;
    this.organizationId = organizationId;
    this.additionalContactInfo = additionalContactInfo;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
    this.tags = tags;
    this.variables = variables;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getPersonId() {
    return personId;
  }
  
  public void setPersonId(Long personId) {
    this.personId = personId;
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

  public EnumSet<UserRole> getRoles() {
    return roles;
  }

  public void setRole(EnumSet<UserRole> roles) {
    this.roles = roles;
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

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  private Long id;
  private Long personId;
  private Long organizationId;
  private String additionalContactInfo;
  private String firstName;
  private String lastName;
  private EnumSet<UserRole> roles;
  private Map<String, String> variables;
  private List<String> tags;
}
