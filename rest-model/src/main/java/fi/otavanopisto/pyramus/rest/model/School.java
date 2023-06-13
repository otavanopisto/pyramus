package fi.otavanopisto.pyramus.rest.model;

import java.util.List;
import java.util.Map;

public class School {

  public School() {
    super();
  }

  public School(Long id, String code, String name, List<String> tags, Long fieldId, String additionalContactInfo, Boolean archived, Map<String, String> variables) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
    this.archived = archived;
    this.tags = tags;
    this.additionalContactInfo = additionalContactInfo;
    this.fieldId = fieldId;
    this.variables = variables;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getAdditionalContactInfo() {
    return additionalContactInfo;
  }
  
  public void setAdditionalContactInfo(String additionalContactInfo) {
    this.additionalContactInfo = additionalContactInfo;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Long getFieldId() {
    return fieldId;
  }

  public void setFieldId(Long fieldId) {
    this.fieldId = fieldId;
  }
 
  public Map<String, String> getVariables() {
    return variables;
  }
  
  public void setVariables(Map<String, String> variables) {
    this.variables = variables;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  public void setStudentGroupId(Long studentGroupId) {
    this.studentGroupId = studentGroupId;
  }

  private Long id;
  private String code;
  private String name;
  private String additionalContactInfo;
  private Boolean archived;
  private List<String> tags;
  private Long fieldId;
  private Long studentGroupId;
  private Map<String, String> variables;
}
