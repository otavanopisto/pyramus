package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplateCourse.class, entityType = TranquilModelType.COMPLETE)
public class TransferCreditTemplateCourseComplete extends TransferCreditTemplateCourseBase {

  public TranquilModelEntity getTransferCreditTemplate() {
    return transferCreditTemplate;
  }

  public void setTransferCreditTemplate(TranquilModelEntity transferCreditTemplate) {
    this.transferCreditTemplate = transferCreditTemplate;
  }

  public TranquilModelEntity getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(TranquilModelEntity courseLength) {
    this.courseLength = courseLength;
  }

  public TranquilModelEntity getSubject() {
    return subject;
  }

  public void setSubject(TranquilModelEntity subject) {
    this.subject = subject;
  }

  private TranquilModelEntity transferCreditTemplate;

  private TranquilModelEntity courseLength;

  private TranquilModelEntity subject;

  public final static String[] properties = {"transferCreditTemplate","courseLength","subject"};
}
