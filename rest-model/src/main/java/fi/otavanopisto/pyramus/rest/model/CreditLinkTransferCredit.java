package fi.otavanopisto.pyramus.rest.model;

public class CreditLinkTransferCredit {
  
  public CreditLinkTransferCredit() {
  }

  public CreditLinkTransferCredit(Long id, Long studentId, TransferCredit credit) {
    super();
    this.id = id;
    this.studentId = studentId;
    this.credit = credit;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public TransferCredit getCredit() {
    return credit;
  }

  public void setCredit(TransferCredit credit) {
    this.credit = credit;
  }

  private Long id;
  private Long studentId;
  private TransferCredit credit;
}
