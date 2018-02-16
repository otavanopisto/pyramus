package fi.otavanopisto.pyramus.koski;

import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;

public class CreditStubCredit {

  public CreditStubCredit(Credit credit, Type type) {
    this.type = type;
    this.credit = credit;
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

  public Credit getCredit() {
    return credit;
  }

  private final Credit credit;
  private final Type type;
}
