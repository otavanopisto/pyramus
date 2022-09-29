package fi.otavanopisto.pyramus.rest.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.otavanopisto.security.ContextReference;

public class StaffMember implements ContextReference {

  public StaffMember() {
    super();
  }

  public StaffMember(Long id, Long personId, Long organizationId, String additionalContactInfo, String firstName, String lastName, String title, UserRole role,
      List<String> tags, Map<String, String> variables, Set<Long> studyProgrammeIds) {
    super();
    this.id = id;
    this.personId = personId;
    this.organizationId = organizationId;
    this.additionalContactInfo = additionalContactInfo;
    this.firstName = firstName;
    this.lastName = lastName;
    this.title = title;
    this.role = role;
    this.tags = tags;
    this.variables = variables;
    this.studyProgrammeIds = studyProgrammeIds;
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

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  public Set<Long> getStudyProgrammeIds() {
    return studyProgrammeIds;
  }

  public void setStudyProgrammeIds(Set<Long> studyProgrammeIds) {
    this.studyProgrammeIds = studyProgrammeIds;
  }

  private Long id;
  private Long personId;
  private Long organizationId;
  private String additionalContactInfo;
  private String firstName;
  private String lastName;
  private String title;
  private UserRole role;
  private Map<String, String> variables;
  private List<String> tags;
  private Set<Long> studyProgrammeIds;
}
