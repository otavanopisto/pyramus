package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public class Module {

  public Module() {
  }
  
  public Module(Long id, String name, OffsetDateTime created, OffsetDateTime lastModified, String description, Boolean archived,
      Long maxParticipantCount, Long creatorId, Long lastModifierId, Set<Long> curriculumIds, List<String> tags, Set<CourseModule> courseModules) {
    super();
    this.id = id;
    this.name = name;
    this.created = created;
    this.lastModified = lastModified;
    this.description = description;
    this.archived = archived;
    this.maxParticipantCount = maxParticipantCount;
    this.creatorId = creatorId;
    this.lastModifierId = lastModifierId;
    this.curriculumIds = curriculumIds;
    this.tags = tags;
    this.courseModules = courseModules;
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

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(OffsetDateTime lastModified) {
    this.lastModified = lastModified;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getMaxParticipantCount() {
    return maxParticipantCount;
  }

  public void setMaxParticipantCount(Long maxParticipantCount) {
    this.maxParticipantCount = maxParticipantCount;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
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

  public Set<Long> getCurriculumIds() {
    return curriculumIds;
  }

  public void setCurriculumIds(Set<Long> curriculumIds) {
    this.curriculumIds = curriculumIds;
  }

  public Set<CourseModule> getCourseModules() {
    return courseModules;
  }

  public void setCourseModules(Set<CourseModule> courseModules) {
    this.courseModules = courseModules;
  }

  private Long id;
  private String name;
  private OffsetDateTime created;
  private OffsetDateTime lastModified;
  private String description;
  private Boolean archived;
  private Long maxParticipantCount;
  private Long creatorId;
  private Long lastModifierId;
  private List<String> tags;
  private Set<Long> curriculumIds;
  private Set<CourseModule> courseModules;
}
