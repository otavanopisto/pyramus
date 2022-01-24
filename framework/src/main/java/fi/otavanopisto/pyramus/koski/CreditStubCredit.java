package fi.otavanopisto.pyramus.koski;

import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditFunding;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;

public class CreditStubCredit {

  public CreditStubCredit(Credit credit, Type type, double courseLength, OpintojenLaajuusYksikko lengthUnit) {
    this.type = type;
    this.credit = credit;
    this.courseLength = courseLength;
    this.courseLenghtUnit = lengthUnit;
  }
  
  public enum Type {
    CREDIT,
    RECOGNIZED
  }

  public Type getType() {
    return type;
  }

  public Date getDate() {
    return credit.getDate();
  }

  public Grade getGrade() {
    return credit.getGrade();
  }
  
  public ArviointiasteikkoYleissivistava getArvosana() {
    if (credit.getGrade() != null) {
      switch (credit.getGrade().getName()) {
        case "4":
          return ArviointiasteikkoYleissivistava.GRADE_4;
        case "5":
          return ArviointiasteikkoYleissivistava.GRADE_5;
        case "6":
          return ArviointiasteikkoYleissivistava.GRADE_6;
        case "7":
          return ArviointiasteikkoYleissivistava.GRADE_7;
        case "8":
          return ArviointiasteikkoYleissivistava.GRADE_8;
        case "9":
          return ArviointiasteikkoYleissivistava.GRADE_9;
        case "10":
          return ArviointiasteikkoYleissivistava.GRADE_10;
        case "H":
          return ArviointiasteikkoYleissivistava.GRADE_H;
        case "S":
          return ArviointiasteikkoYleissivistava.GRADE_S;
      }
    }
    
    return null;
  }

  public Credit getCredit() {
    return credit;
  }
  
  public TransferCreditFunding getTransferCreditFunding() {
    return credit instanceof TransferCredit ? ((TransferCredit) credit).getFunding() : null;
  }

  public double getCourseLength() {
    return courseLength;
  }

  public OpintojenLaajuusYksikko getCourseLenghtUnit() {
    return courseLenghtUnit;
  }

  private final Credit credit;
  private final Type type;
  private final double courseLength;
  private final OpintojenLaajuusYksikko courseLenghtUnit;
}
