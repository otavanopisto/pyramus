package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

import org.joda.time.DateTime;

public class StudentGroup {

  public StudentGroup() {
    super();
  }

  public StudentGroup(Long id, String name, String description, DateTime beginDate, Long creatorId, DateTime created, Long lastModifierId,
      DateTime lastModified, List<String> tags, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.beginDate = beginDate;
    this.archived = archived;
    this.creatorId = creatorId;
    this.created = created;
    this.lastModifierId = lastModifierId;
    this.lastModified = lastModified;
    this.tags = tags;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DateTime getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(DateTime beginDate) {
    this.beginDate = beginDate;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public Long getLastModifierId() {
    return lastModifierId;
  }

  public void setLastModifierId(Long lastModifierId) {
    this.lastModifierId = lastModifierId;
  }

  public DateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(DateTime lastModified) {
    this.lastModified = lastModified;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  private Long id;
  private String name;
  private String description;
  private DateTime beginDate;
  private Boolean archived;
  private Long creatorId;
  private DateTime created;
  private Long lastModifierId;
  private DateTime lastModified;
  private List<String> tags;
}