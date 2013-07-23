package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplateCourse.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditTemplateCourseCompact extends TransferCreditTemplateCourseBase {

  public Long getTransferCreditTemplate_id() {
    return transferCreditTemplate_id;
  }

  public void setTransferCreditTemplate_id(Long transferCreditTemplate_id) {
    this.transferCreditTemplate_id = transferCreditTemplate_id;
  }

  public Long getCourseLength_id() {
    return courseLength_id;
  }

  public void setCourseLength_id(Long courseLength_id) {
    this.courseLength_id = courseLength_id;
  }

  public Long getSubject_id() {
    return subject_id;
  }

  public void setSubject_id(Long subject_id) {
    this.subject_id = subject_id;
  }

  private Long transferCreditTemplate_id;

  private Long courseLength_id;

  private Long subject_id;

  public final static String[] properties = {"transferCreditTemplate","courseLength","subject"};
}
