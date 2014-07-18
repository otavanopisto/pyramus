package fi.pyramus.rest.model;

import java.util.List;

public class School {

  public School() {
    super();
  }

  public School(Long id, String code, String name, List<String> tags, Long fieldId, Boolean archived) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
    this.archived = archived;
    this.tags = tags;
    this.fieldId = fieldId;
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

  private Long id;
  private String code;
  private String name;
  private Boolean archived;
  private List<String> tags;
  private Long fieldId;
}
