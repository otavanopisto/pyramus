package fi.otavanopisto.pyramus.rest.model.worklist;

public class CourseBillingRestModel {

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

  public Double getDefaultPrice() {
    return defaultPrice;
  }

  public void setDefaultPrice(Double defaultPrice) {
    this.defaultPrice = defaultPrice;
  }

  public Double getDefault2021Price() {
    return default2021Price;
  }

  public void setDefault2021Price(Double default2021Price) {
    this.default2021Price = default2021Price;
  }

  public Double getDefault2021PointPrice() {
    return default2021PointPrice;
  }

  public void setDefault2021PointPrice(Double default2021PointPrice) {
    this.default2021PointPrice = default2021PointPrice;
  }

  private String highSchoolBillingNumber;
  private String elementaryBillingNumber;
  private Double defaultPrice;
  private Double default2021Price;
  private Double default2021PointPrice;

}
