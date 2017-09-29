package fi.otavanopisto.pyramus.events;

public class TransferCreditEvent {

  public TransferCreditEvent(Long studentId, Long transferCreditId) {
    this.studentId = studentId;
    this.transferCreditId = transferCreditId;
  }

  public Long getStudentId() {
    return studentId;
  }

  public Long getTransferCreditId() {
    return transferCreditId;
  }

  private final Long transferCreditId;
  private final Long studentId;
}
