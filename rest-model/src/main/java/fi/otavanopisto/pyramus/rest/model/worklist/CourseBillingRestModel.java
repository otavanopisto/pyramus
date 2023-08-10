package fi.otavanopisto.pyramus.rest.model.worklist;

public class CourseBillingRestModel {

  public Double getElementaryPrice() {
    return elementaryPrice;
  }

  public void setElementaryPrice(Double elementaryPrice) {
    this.elementaryPrice = elementaryPrice;
  }

  public Double getHighSchoolPrice() {
    return highSchoolPrice;
  }

  public void setHighSchoolPrice(Double highSchoolPrice) {
    this.highSchoolPrice = highSchoolPrice;
  }

  public Double getHighSchoolPointPrice() {
    return highSchoolPointPrice;
  }

  public void setHighSchoolPointPrice(Double highSchoolPointPrice) {
    this.highSchoolPointPrice = highSchoolPointPrice;
  }

  public String getHighSchoolBillingNumber() {
    return highSchoolBillingNumber;
  }

  public void setHighSchoolBillingNumber(String highSchoolBillingNumber) {
    this.highSchoolBillingNumber = highSchoolBillingNumber;
  }

  public String getElementaryBillingNumber() {
    return elementaryBillingNumber;
  }

  public void setElementaryBillingNumber(String elementaryBillingNumber) {
    this.elementaryBillingNumber = elementaryBillingNumber;
  }

  private String highSchoolBillingNumber;
  private String elementaryBillingNumber;
  private Double elementaryPrice;
  private Double highSchoolPrice;
  private Double highSchoolPointPrice;

}
