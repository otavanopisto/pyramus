package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Course {

  public Course() {
  }
  
  public Course(String name, OffsetDateTime created, OffsetDateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, OffsetDateTime beginDate, OffsetDateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, OffsetDateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Set<Long> curriculumIds, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags, Long organizationId) {
    this(null, name, created, lastModified, description, archived, courseNumber, maxParticipantCount, beginDate, endDate, 
        nameExtension, localTeachingDays, teachingHours, distanceTeachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, subjectId, curriculumIds, length, lengthUnitId, moduleId, stateId, typeId, variables, tags, organizationId);
  }

  public Course(Long id, String name, OffsetDateTime created, OffsetDateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, OffsetDateTime beginDate, OffsetDateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingHours, Double distanceTeachingDays, Double assessingHours, Double planningHours, OffsetDateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Set<Long> curriculumIds, Double length, Long lengthUnitId, Long moduleId, Long stateId, Long typeId, 
      Map<String, String> variables, List<String> tags, Long organizationId) {
    super();
    this.id = id;
    this.organizationId = organizationId;
    this.name = name;
    this.created = created;
    this.lastModified = lastModified;
    this.description = description;
    this.archived = archived;
    this.courseNumber = courseNumber;
    this.maxParticipantCount = maxParticipantCount;
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.nameExtension = nameExtension;
    this.localTeachingDays = localTeachingDays;
    this.teachingHours = teachingHours;
    this.distanceTeachingHours = distanceTeachingHours;
    this.distanceTeachingDays = distanceTeachingDays;
    this.assessingHours = assessingHours;
    this.planningHours = planningHours;
    
    this.enrolmentTimeEnd = enrolmentTimeEnd;
    this.creatorId = creatorId;
    this.lastModifierId = lastModifierId;
    this.subjectId = subjectId;
    this.curriculumIds = curriculumIds;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.moduleId = moduleId;
    this.stateId = stateId;
    this.typeId = typeId;
    this.tags = tags;
    this.variables = variables;
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

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Long getMaxParticipantCount() {
    return maxParticipantCount;
  }

  public void setMaxParticipantCount(Long maxParticipantCount) {
    this.maxParticipantCount = maxParticipantCount;
  }

  public OffsetDateTime getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(OffsetDateTime beginDate) {
    this.beginDate = beginDate;
  }

  public OffsetDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public String getNameExtension() {
    return nameExtension;
  }

  public void setNameExtension(String nameExtension) {
    this.nameExtension = nameExtension;
  }

  public Double getLocalTeachingDays() {
    return localTeachingDays;
  }

  public void setLocalTeachingDays(Double localTeachingDays) {
    this.localTeachingDays = localTeachingDays;
  }

  public Double getTeachingHours() {
    return teachingHours;
  }

  public void setTeachingHours(Double teachingHours) {
    this.teachingHours = teachingHours;
  }

  public Double getDistanceTeachingDays() {
    return distanceTeachingDays;
  }

  public void setDistanceTeachingDays(Double distanceTeachingDays) {
    this.distanceTeachingDays = distanceTeachingDays;
  }

  public Double getAssessingHours() {
    return assessingHours;
  }

  public void setAssessingHours(Double assessingHours) {
    this.assessingHours = assessingHours;
  }

  public Double getPlanningHours() {
    return planningHours;
  }

  public void setPlanningHours(Double planningHours) {
    this.planningHours = planningHours;
  }

  public OffsetDateTime getEnrolmentTimeEnd() {
    return enrolmentTimeEnd;
  }

  public void setEnrolmentTimeEnd(OffsetDateTime enrolmentTimeEnd) {
    this.enrolmentTimeEnd = enrolmentTimeEnd;
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

  public Long getSubjectId() {
    return subjectId;
  }
  
  public void setSubjectId(Long subjectId) {
    this.subjectId = subjectId;
  }
  
  public Double getLength() {
    return length;
  }
  
  public void setLength(Double length) {
    this.length = length;
  }
  
  public Long getLengthUnitId() {
    return lengthUnitId;
  }
  
  public void setLengthUnitId(Long lengthUnitId) {
    this.lengthUnitId = lengthUnitId;
  }

  public Long getModuleId() {
    return moduleId;
  }

  public void setModuleId(Long moduleId) {
    this.moduleId = moduleId;
  }

  public Long getStateId() {
    return stateId;
  }

  public void setStateId(Long stateId) {
    this.stateId = stateId;
  }
  
  public Long getTypeId() {
    return typeId;
  }
  
  public void setTypeId(Long typeId) {
    this.typeId = typeId;
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

  public Double getDistanceTeachingHours() {
    return distanceTeachingHours;
  }

  public void setDistanceTeachingHours(Double distanceTeachingHours) {
    this.distanceTeachingHours = distanceTeachingHours;
  }

  public Set<Long> getCurriculumIds() {
    return curriculumIds;
  }

  public void setCurriculumIds(Set<Long> curriculumIds) {
    this.curriculumIds = curriculumIds;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  private Long id;
  private Long organizationId;
  private String name;
  private OffsetDateTime created;
  private OffsetDateTime lastModified;
  private String description;
  private Boolean archived;
  private Integer courseNumber;
  private Long maxParticipantCount;
  private OffsetDateTime beginDate;
  private OffsetDateTime endDate;
  private String nameExtension;
  private Double localTeachingDays;
  private Double teachingHours;
  private Double distanceTeachingHours;
  private Double distanceTeachingDays;
  private Double assessingHours;
  private Double planningHours;
  private OffsetDateTime enrolmentTimeEnd;
  private Long creatorId;
  private Long lastModifierId;
  private Long subjectId;
  private Double length;
  private Long lengthUnitId;
  private Long moduleId;
  private Long stateId;
  private Long typeId;
  private Map<String, String> variables;
  private List<String> tags;
  private Set<Long> curriculumIds;
}
