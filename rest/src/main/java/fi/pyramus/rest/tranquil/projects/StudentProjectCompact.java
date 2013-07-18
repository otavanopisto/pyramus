package fi.pyramus.rest.tranquil.projects;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.projects.StudentProject.class, entityType = TranquilModelType.COMPACT)
public class StudentProjectCompact extends StudentProjectBase {

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

  private Long creator_id;

  private Long lastModifier_id;

  private Long optionalStudiesLength_id;

  private Long student_id;

  private Long project_id;

  private java.util.List<Long> studentProjectModules_ids;

  private java.util.List<Long> tags_ids;

  public final static String[] properties = {"creator","lastModifier","optionalStudiesLength","student","project","studentProjectModules","tags"};
}
