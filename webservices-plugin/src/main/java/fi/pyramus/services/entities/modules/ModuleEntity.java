package fi.pyramus.services.entities.modules;

import java.util.Date;

import fi.pyramus.services.entities.base.SubjectEntity;
import fi.pyramus.services.entities.courses.CourseEducationTypeEntity;
import fi.pyramus.services.entities.users.UserEntity;

public class ModuleEntity {

  public ModuleEntity() {
  }
  
  public ModuleEntity(Long id, String name, String[] tags, UserEntity creator, Date created, UserEntity lastModifier, Date lastModified, String description,
      SubjectEntity subject, Integer courseNumber, Double courseLength, Long courseLengthUnitId, CourseEducationTypeEntity[] courseEducationTypes,
      Boolean archived, ModuleComponentEntity[] moduleComponents) {
    super();
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.creator = creator;
    this.created = created;
    this.lastModifier = lastModifier;
    this.lastModified = lastModified;
    this.description = description;
    this.subject = subject;
    this.courseNumber = courseNumber;
    this.courseLength = courseLength;
    this.courseLengthUnitId = courseLengthUnitId;
    this.courseEducationTypes = courseEducationTypes;
    this.archived = archived;
    this.moduleComponents = moduleComponents;
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

  public CourseEducationTypeEntity[] getCourseEducationTypes() {
    return courseEducationTypes;
  }

  public void setCourseEducationTypes(CourseEducationTypeEntity[] courseEducationTypes) {
    this.courseEducationTypes = courseEducationTypes;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public ModuleComponentEntity[] getModuleComponents() {
    return moduleComponents;
  }

  public void setModuleComponents(ModuleComponentEntity[] moduleComponents) {
    this.moduleComponents = moduleComponents;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  private Long id;
  private String name;
  private UserEntity creator;
  private Date created;
  private UserEntity lastModifier;
  private Date lastModified;
  private String description;
  private SubjectEntity subject;
  private Integer courseNumber;
  private Double courseLength;
  private Long courseLengthUnitId;
  private CourseEducationTypeEntity[] courseEducationTypes;
  private Boolean archived;
  private ModuleComponentEntity[] moduleComponents;
  private String[] tags;
}
