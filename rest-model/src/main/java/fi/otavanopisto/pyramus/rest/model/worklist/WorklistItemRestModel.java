package fi.otavanopisto.pyramus.rest.model.worklist;

import java.time.LocalDate;
import java.util.Set;

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

  public LocalDate getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(LocalDate entryDate) {
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

  public Boolean getRemovable() {
    return removable;
  }

  public void setRemovable(Boolean removable) {
    this.removable = removable;
  }

  public Set<String> getEditableFields() {
    return editableFields;
  }

  public void setEditableFields(Set<String> editableFields) {
    this.editableFields = editableFields;
  }

  public Long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Long templateId) {
    this.templateId = templateId;
  }

  public String getBillingNumber() {
    return billingNumber;
  }

  public void setBillingNumber(String billingNumber) {
    this.billingNumber = billingNumber;
  }

  private Long id;
  private Long templateId;
  private LocalDate entryDate;
  private String description;
  private Double price;
  private Double factor;
  private String billingNumber;
  private WorklistItemCourseAssessmentRestModel courseAssessment;
  private Set<String> editableFields;
  private Boolean removable;

}
