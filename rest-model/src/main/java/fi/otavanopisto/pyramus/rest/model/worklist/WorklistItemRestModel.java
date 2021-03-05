package fi.otavanopisto.pyramus.rest.model.worklist;

import java.time.LocalDateTime;

/**
 * Return object when querying worklist items. Includes detailed information
 * about the course assessment, if the item is associated with one.
 */
public class WorklistItemRestModel {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(LocalDateTime entryDate) {
    this.entryDate = entryDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Double getFactor() {
    return factor;
  }

  public void setFactor(Double factor) {
    this.factor = factor;
  }

  public WorklistItemCourseAssessmentRestModel getCourseAssessment() {
    return courseAssessment;
  }

  public void setCourseAssessment(WorklistItemCourseAssessmentRestModel courseAssessment) {
    this.courseAssessment = courseAssessment;
  }

  public Boolean getEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public Boolean getRemovable() {
    return removable;
  }

  public void setRemovable(Boolean removable) {
    this.removable = removable;
  }

  private Long id;
  private LocalDateTime entryDate;
  private String description;
  private Double price;
  private Double factor;
  private WorklistItemCourseAssessmentRestModel courseAssessment;
  private Boolean editable;
  private Boolean removable;

}
