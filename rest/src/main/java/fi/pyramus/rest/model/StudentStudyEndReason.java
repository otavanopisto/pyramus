package fi.pyramus.rest.model;

public class StudentStudyEndReason {

  public StudentStudyEndReason() {
    super();
  }

  public StudentStudyEndReason(Long id, String name, Long parentReasonId) {
    this();
    this.id = id;
    this.name = name;
    this.parentReasonId = parentReasonId;
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

  public Long getParentReasonId() {
    return parentReasonId;
  }

  public void setParentReasonId(Long parentReasonId) {
    this.parentReasonId = parentReasonId;
  }

  private Long id;
  private String name;
  private Long parentReasonId;
}
