package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.courses.Course.class, entityType = TranquilModelType.COMPACT)
public class CourseEntity implements fi.tranquil.TranquilModelEntity {

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

  public java.util.Date getCreated() {
    return created;
  }

  public void setCreated(java.util.Date created) {
    this.created = created;
  }

  public java.util.Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(java.util.Date lastModified) {
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

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getMaxParticipantCount() {
    return maxParticipantCount;
  }

  public void setMaxParticipantCount(Long maxParticipantCount) {
    this.maxParticipantCount = maxParticipantCount;
  }

  public java.util.Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(java.util.Date beginDate) {
    this.beginDate = beginDate;
  }

  public java.util.Date getEndDate() {
    return endDate;
  }

  public void setEndDate(java.util.Date endDate) {
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
  
  public Double getCourseLength() {
    return courseLength;
  }
  
  public void setCourseLength(Double courseLength) {
    this.courseLength = courseLength;
  }

  public java.util.Date getEnrolmentTimeEnd() {
    return enrolmentTimeEnd;
  }

  public void setEnrolmentTimeEnd(java.util.Date enrolmentTimeEnd) {
    this.enrolmentTimeEnd = enrolmentTimeEnd;
  }

  public Long getCreator_id() {
    return creator_id;
  }

  public void setCreator_id(Long creator_id) {
    this.creator_id = creator_id;
  }

  public Long getLastModifier_id() {
    return lastModifier_id;
  }

  public void setLastModifier_id(Long lastModifier_id) {
    this.lastModifier_id = lastModifier_id;
  }

  public Long getSubject_id() {
    return subject_id;
  }

  public void setSubject_id(Long subject_id) {
    this.subject_id = subject_id;
  }

  public Long getCourseEducationTypeByEducationTypeId_id() {
    return courseEducationTypeByEducationTypeId_id;
  }

  public void setCourseEducationTypeByEducationTypeId_id(Long courseEducationTypeByEducationTypeId_id) {
    this.courseEducationTypeByEducationTypeId_id = courseEducationTypeByEducationTypeId_id;
  }

  public Long getCourseLength_id() {
    return courseLength_id;
  }

  public void setCourseLength_id(Long courseLength_id) {
    this.courseLength_id = courseLength_id;
  }

  public Long getModule_id() {
    return module_id;
  }

  public void setModule_id(Long module_id) {
    this.module_id = module_id;
  }

  public Long getState_id() {
    return state_id;
  }

  public void setState_id(Long state_id) {
    this.state_id = state_id;
  }

  public java.util.List<Long> getCourseEducationTypes_ids() {
    return courseEducationTypes_ids;
  }

  public void setCourseEducationTypes_ids(java.util.List<Long> courseEducationTypes_ids) {
    this.courseEducationTypes_ids = courseEducationTypes_ids;
  }

  public java.util.List<Long> getVariables_ids() {
    return variables_ids;
  }

  public void setVariables_ids(java.util.List<Long> variables_ids) {
    this.variables_ids = variables_ids;
  }

  public java.util.List<Long> getCourseComponents_ids() {
    return courseComponents_ids;
  }

  public void setCourseComponents_ids(java.util.List<Long> courseComponents_ids) {
    this.courseComponents_ids = courseComponents_ids;
  }

  public java.util.List<Long> getCourseUsers_ids() {
    return courseUsers_ids;
  }

  public void setCourseUsers_ids(java.util.List<Long> courseUsers_ids) {
    this.courseUsers_ids = courseUsers_ids;
  }

  public java.util.List<Long> getCourseStudents_ids() {
    return courseStudents_ids;
  }

  public void setCourseStudents_ids(java.util.List<Long> courseStudents_ids) {
    this.courseStudents_ids = courseStudents_ids;
  }

  public java.util.List<Long> getStudentCourseResources_ids() {
    return studentCourseResources_ids;
  }

  public void setStudentCourseResources_ids(java.util.List<Long> studentCourseResources_ids) {
    this.studentCourseResources_ids = studentCourseResources_ids;
  }

  public java.util.List<Long> getBasicCourseResources_ids() {
    return basicCourseResources_ids;
  }

  public void setBasicCourseResources_ids(java.util.List<Long> basicCourseResources_ids) {
    this.basicCourseResources_ids = basicCourseResources_ids;
  }

  public java.util.List<Long> getGradeCourseResources_ids() {
    return gradeCourseResources_ids;
  }

  public void setGradeCourseResources_ids(java.util.List<Long> gradeCourseResources_ids) {
    this.gradeCourseResources_ids = gradeCourseResources_ids;
  }

  public java.util.List<Long> getOtherCosts_ids() {
    return otherCosts_ids;
  }

  public void setOtherCosts_ids(java.util.List<Long> otherCosts_ids) {
    this.otherCosts_ids = otherCosts_ids;
  }

  public java.util.List<Long> getTags_ids() {
    return tags_ids;
  }

  public void setTags_ids(java.util.List<Long> tags_ids) {
    this.tags_ids = tags_ids;
  }

  private Long id;

  private String name;

  private java.util.Date created;

  private java.util.Date lastModified;

  private String description;

  private Boolean archived;

  private Integer courseNumber;

  private Long version;

  private Long maxParticipantCount;

  private java.util.Date beginDate;

  private java.util.Date endDate;

  private String nameExtension;

  private Double localTeachingDays;

  private Double teachingHours;

  private Double distanceTeachingDays;

  private Double assessingHours;

  private Double planningHours;
  
  private Double courseLength;

  private java.util.Date enrolmentTimeEnd;

  private Long creator_id;

  private Long lastModifier_id;

  private Long subject_id;

  private Long courseEducationTypeByEducationTypeId_id;

  private Long courseLength_id;

  private Long module_id;

  private Long state_id;

  private java.util.List<Long> courseEducationTypes_ids;

  private java.util.List<Long> variables_ids;

  private java.util.List<Long> courseComponents_ids;

  private java.util.List<Long> courseUsers_ids;

  private java.util.List<Long> courseStudents_ids;

  private java.util.List<Long> studentCourseResources_ids;

  private java.util.List<Long> basicCourseResources_ids;

  private java.util.List<Long> gradeCourseResources_ids;

  private java.util.List<Long> otherCosts_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"id","name","created","lastModified","description","archived","courseNumber","version","maxParticipantCount","beginDate","endDate","nameExtension","localTeachingDays","teachingHours","distanceTeachingDays","assessingHours","planningHours","enrolmentTimeEnd","creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","module","state","courseEducationTypes","variables","courseComponents","courseUsers","courseStudents","studentCourseResources","basicCourseResources","gradeCourseResources","otherCosts","tags"};
}
