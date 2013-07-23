package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.StudentProject.class, entityType = TranquilModelType.COMPACT)
public class StudentProjectEntity implements fi.tranquil.TranquilModelEntity {

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

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
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

  public Long getOptionalStudiesLength_id() {
    return optionalStudiesLength_id;
  }

  public void setOptionalStudiesLength_id(Long optionalStudiesLength_id) {
    this.optionalStudiesLength_id = optionalStudiesLength_id;
  }

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  public Long getProject_id() {
    return project_id;
  }

  public void setProject_id(Long project_id) {
    this.project_id = project_id;
  }

  public java.util.List<Long> getStudentProjectModules_ids() {
    return studentProjectModules_ids;
  }

  public void setStudentProjectModules_ids(java.util.List<Long> studentProjectModules_ids) {
    this.studentProjectModules_ids = studentProjectModules_ids;
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

  private Boolean archived;

  private String description;

  private Long version;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long creator_id;

  private Long lastModifier_id;

  private Long optionalStudiesLength_id;

  private Long student_id;

  private Long project_id;

  private java.util.List<Long> studentProjectModules_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"id","name","created","lastModified","archived","description","version","optionality","creator","lastModifier","optionalStudiesLength","student","project","studentProjectModules","tags"};
}
