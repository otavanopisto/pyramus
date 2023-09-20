package fi.otavanopisto.pyramus.rest.model.worklist;

public class CourseBillingRestModel {

  public Double getAssessment2016Price() {
    return assessment2016Price;
  }

  public void setAssessment2016Price(Double assessment2016Price) {
    this.assessment2016Price = assessment2016Price;
  }

  public Double getAssessment2021op1Price() {
    return assessment2021op1Price;
  }

  public void setAssessment2021op1Price(Double assessment2021op1Price) {
    this.assessment2021op1Price = assessment2021op1Price;
  }

  public Double getAssessment2021op2Price() {
    return assessment2021op2Price;
  }

  public void setAssessment2021op2Price(Double assessment2021op2Price) {
    this.assessment2021op2Price = assessment2021op2Price;
  }

  public Double getAssessment2021op3Price() {
    return assessment2021op3Price;
  }

  public void setAssessment2021op3Price(Double assessment2021op3Price) {
    this.assessment2021op3Price = assessment2021op3Price;
  }

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
  private Double assessment2016Price;
  private Double assessment2021op1Price;
  private Double assessment2021op2Price;
  private Double assessment2021op3Price;

}
