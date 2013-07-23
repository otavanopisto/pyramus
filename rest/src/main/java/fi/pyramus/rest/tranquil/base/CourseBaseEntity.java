package fi.pyramus.rest.tranquil.base;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.base.CourseBase.class, entityType = TranquilModelType.COMPACT)
public class CourseBaseEntity implements fi.tranquil.TranquilModelEntity {

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

  private Long id;

  private String name;

  private java.util.Date created;

  private java.util.Date lastModified;

  private String description;

  private Boolean archived;

  private Integer courseNumber;

  private Long version;

  private Long maxParticipantCount;

  private Long creator_id;

  private Long lastModifier_id;

  private Long subject_id;

  private Long courseEducationTypeByEducationTypeId_id;

  private Long courseLength_id;

  private java.util.List<Long> courseEducationTypes_ids;

  private java.util.List<Long> variables_ids;

  public final static String[] properties = {"id","name","created","lastModified","description","archived","courseNumber","version","maxParticipantCount","creator","lastModifier","subject","courseEducationTypeByEducationTypeId","courseLength","courseEducationTypes","variables"};
}
