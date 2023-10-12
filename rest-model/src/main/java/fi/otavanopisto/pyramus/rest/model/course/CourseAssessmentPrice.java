package fi.otavanopisto.pyramus.rest.model.course;

public class CourseAssessmentPrice {
  
  public CourseAssessmentPrice() {
  }

  public CourseAssessmentPrice(Double price) {
    this.price = price;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  private Double price;
}
