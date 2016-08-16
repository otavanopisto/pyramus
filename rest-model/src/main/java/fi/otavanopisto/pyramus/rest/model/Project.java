package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

import java.time.OffsetDateTime;

public class Project {

  public Project() {
  }

  public Project(Long id, String name, String description, Double optionalStudiesLength, Long optionalStudiesLengthUnitId, OffsetDateTime created, Long creatorId,
      OffsetDateTime lastModified, Long lastModifierId, List<String> tags, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.optionalStudiesLength = optionalStudiesLength;
    this.optionalStudiesLengthUnitId = optionalStudiesLengthUnitId;
    this.created = created;
    this.creatorId = creatorId;
    this.lastModified = lastModified;
    this.lastModifierId = lastModifierId;
    this.tags = tags;
    this.archived = archived;
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

  public Double getOptionalStudiesLength() {
    return optionalStudiesLength;
  }

  public void setOptionalStudiesLength(Double optionalStudiesLength) {
    this.optionalStudiesLength = optionalStudiesLength;
  }

  public Long getOptionalStudiesLengthUnitId() {
    return optionalStudiesLengthUnitId;
  }

  public void setOptionalStudiesLengthUnitId(Long optionalStudiesLengthUnitId) {
    this.optionalStudiesLengthUnitId = optionalStudiesLengthUnitId;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
  }

  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(OffsetDateTime lastModified) {
    this.lastModified = lastModified;
  }

  public Long getLastModifierId() {
    return lastModifierId;
  }

  public void setLastModifierId(Long lastModifierId) {
    this.lastModifierId = lastModifierId;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private String name;
  private String description;
  private Double optionalStudiesLength;
  private Long optionalStudiesLengthUnitId;
  private OffsetDateTime created;
  private Long creatorId;
  private OffsetDateTime lastModified;
  private Long lastModifierId;
  private List<String> tags;
  private Boolean archived;
}