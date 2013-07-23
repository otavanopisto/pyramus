package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CourseAssessmentRequest.class, entityType = TranquilModelType.COMPACT)
public class CourseAssessmentRequestEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRequestText() {
    return requestText;
  }

  public void setRequestText(String requestText) {
    this.requestText = requestText;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public java.util.Date getCreated() {
    return created;
  }

  public void setCreated(java.util.Date created) {
    this.created = created;
  }

  public Long getCourseStudent_id() {
    return courseStudent_id;
  }

  public void setCourseStudent_id(Long courseStudent_id) {
    this.courseStudent_id = courseStudent_id;
  }

  private Long id;

  private String requestText;

  private Boolean archived;

  private java.util.Date created;

  private Long courseStudent_id;

  public final static String[] properties = {"id","requestText","archived","created","courseStudent"};
}
