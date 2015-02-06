package fi.pyramus.rest.model;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class Course {

  public Course() {
  }
  
  public Course(String name, DateTime created, DateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, DateTime beginDate, DateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingDays, Double assessingHours, Double planningHours, DateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Double length, Long lengthUnitId, Long moduleId, Long stateId, Map<String, String> variables, List<String> tags) {
    this(null, name, created, lastModified, description, archived, courseNumber, maxParticipantCount, beginDate, endDate, 
        nameExtension, localTeachingDays, teachingHours, distanceTeachingDays, assessingHours, planningHours, enrolmentTimeEnd, 
        creatorId, lastModifierId, subjectId, length, lengthUnitId, moduleId, stateId, variables, tags);
  }

  public Course(Long id, String name, DateTime created, DateTime lastModified, String description, Boolean archived, Integer courseNumber, 
      Long maxParticipantCount, DateTime beginDate, DateTime endDate, String nameExtension, Double localTeachingDays, Double teachingHours,
      Double distanceTeachingDays, Double assessingHours, Double planningHours, DateTime enrolmentTimeEnd, Long creatorId,
      Long lastModifierId, Long subjectId, Double length, Long lengthUnitId, Long moduleId, Long stateId, Map<String, String> variables, List<String> tags) {
    super();
    this.id = id;
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
    this.distanceTeachingDays = distanceTeachingDays;
    this.assessingHours = assessingHours;
    this.planningHours = planningHours;
    this.enrolmentTimeEnd = enrolmentTimeEnd;
    this.creatorId = creatorId;
    this.lastModifierId = lastModifierId;
    this.subjectId = subjectId;
    this.length = length;
    this.lengthUnitId = lengthUnitId;
    this.moduleId = moduleId;
    this.stateId = stateId;
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

  public DateTime getCreated() {
    return created;
  }

  public void setCreated(DateTime created) {
    this.created = created;
  }

  public DateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(DateTime lastModified) {
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

  public DateTime getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(DateTime beginDate) {
    this.beginDate = beginDate;
  }

  public DateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(DateTime endDate) {
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

  public DateTime getEnrolmentTimeEnd() {
    return enrolmentTimeEnd;
  }

  public void setEnrolmentTimeEnd(DateTime enrolmentTimeEnd) {
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
  private String name;
  private DateTime created;
  private DateTime lastModified;
  private String description;
  private Boolean archived;
  private Integer courseNumber;
  private Long maxParticipantCount;
  private DateTime beginDate;
  private DateTime endDate;
  private String nameExtension;
  private Double localTeachingDays;
  private Double teachingHours;
  private Double distanceTeachingDays;
  private Double assessingHours;
  private Double planningHours;
  private DateTime enrolmentTimeEnd;
  private Long creatorId;
  private Long lastModifierId;
  private Long subjectId;
  private Double length;
  private Long lengthUnitId;
  private Long moduleId;
  private Long stateId;
  private Map<String, String> variables;
  private List<String> tags;
}
