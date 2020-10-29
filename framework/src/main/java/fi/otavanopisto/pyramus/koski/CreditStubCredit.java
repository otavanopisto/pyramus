package fi.otavanopisto.pyramus.koski;

import java.util.Date;

import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditFunding;

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
  
  public TransferCreditFunding getTransferCreditFunding() {
    return credit instanceof TransferCredit ? ((TransferCredit) credit).getFunding() : null;
  }

  private final Credit credit;
  private final Type type;
}
