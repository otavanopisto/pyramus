package fi.pyramus.services.entities.courses;

import java.util.Date;
import fi.pyramus.services.entities.base.SubjectEntity;
import fi.pyramus.services.entities.users.UserEntity;

public class CourseEntity {

  public CourseEntity() {
  }
  
  public CourseEntity(Long id, String name, String nameExtension, String[] tags, UserEntity creator, Date created, UserEntity lastModifier, Date lastModified,
      String description, SubjectEntity subject, Integer courseNumber, Double courseLength, Long courseLengthUnitId,
      CourseEducationTypeEntity[] educationTypes, Boolean archived, CourseComponentEntity[] courseComponents, Long moduleId, Date beginDate, Date endDate) {
    super();
    this.id = id;
    this.name = name;
    this.nameExtension = nameExtension;
    this.creator = creator;
    this.created = created;
    this.lastModifier = lastModifier;
    this.lastModified = lastModified;
    this.description = description;
    this.subject = subject;
    this.courseNumber = courseNumber;
    this.courseLength = courseLength;
    this.courseLengthUnitId = courseLengthUnitId;
    this.educationTypes = educationTypes;
    this.archived = archived;
    this.courseComponents = courseComponents;
    this.moduleId = moduleId;
    this.beginDate = beginDate;
    this.endDate = endDate;
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

  public String getNameExtension() {
    return nameExtension;
  }

  public void setNameExtension(String nameExtension) {
    this.nameExtension = nameExtension;
  }

  public UserEntity getCreator() {
    return creator;
  }

  public void setCreator(UserEntity creator) {
    this.creator = creator;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public UserEntity getLastModifier() {
    return lastModifier;
  }

  public void setLastModifier(UserEntity lastModifier) {
    this.lastModifier = lastModifier;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SubjectEntity getSubject() {
    return subject;
  }

  public void setSubject(SubjectEntity subject) {
    this.subject = subject;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Double getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(Double courseLength) {
    this.courseLength = courseLength;
  }

  public Long getCourseLengthUnitId() {
    return courseLengthUnitId;
  }

  public void setCourseLengthUnitId(Long courseLengthUnitId) {
    this.courseLengthUnitId = courseLengthUnitId;
  }

  public CourseEducationTypeEntity[] getEducationTypes() {
    return educationTypes;
  }

  public void setEducationTypes(CourseEducationTypeEntity[] educationTypes) {
    this.educationTypes = educationTypes;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public CourseComponentEntity[] getCourseComponents() {
    return courseComponents;
  }

  public void setCourseComponents(CourseComponentEntity[] courseComponents) {
    this.courseComponents = courseComponents;
  }

  public Long getModuleId() {
    return moduleId;
  }

  public void setModuleId(Long moduleId) {
    this.moduleId = moduleId;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  private Long id;
  private String name;
  private String nameExtension;
  private UserEntity creator;
  private Date created;
  private UserEntity lastModifier;
  private Date lastModified;
  private String description;
  private SubjectEntity subject;
  private Integer courseNumber;
  private Double courseLength;
  private Long courseLengthUnitId;
  private CourseEducationTypeEntity[] educationTypes;
  private Boolean archived;
  private CourseComponentEntity[] courseComponents;
  private Long moduleId;
  private Date beginDate;
  private Date endDate;
  private String[] tags;
}
