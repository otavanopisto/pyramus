package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

public class CourseAssessmentRequest {

  public CourseAssessmentRequest() {
    super();
  }

  public CourseAssessmentRequest(Long id, Long courseStudentId, OffsetDateTime created, String requestText, boolean archived, boolean handled) {
    super();
    this.id = id;
    this.courseStudentId = courseStudentId;
    this.created = created;
    this.requestText = requestText;
    this.archived = archived;
    this.handled = handled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public String getRequestText() {
    return requestText;
  }

  public void setRequestText(String requestText) {
    this.requestText = requestText;
  }

  public boolean getArchived() {
    return archived;
  }

  public void setArchived(boolean archived) {
    this.archived = archived;
  }

  public boolean getHandled() {
    return handled;
  }

  public void setHandled(boolean handled) {
    this.handled = handled;
  }

  public boolean getLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  private Long id;
  private Long courseStudentId;
  private OffsetDateTime created;
  private String requestText;
  private boolean archived;
  private boolean handled;
  private boolean locked;
}
