package fi.otavanopisto.pyramus.rest.model.worklist;

public class WorklistItemBilledPriceRestModel {

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Boolean getEditable() {
    return editable;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public String getAssessmentIdentifier() {
    return assessmentIdentifier;
  }

  public void setAssessmentIdentifier(String assessmentIdentifier) {
    this.assessmentIdentifier = assessmentIdentifier;
  }

  private String assessmentIdentifier;
  private Double price;
  private Boolean editable;

}
