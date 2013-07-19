package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplateCourse.class, entityType = TranquilModelType.UPDATE)
public class TransferCreditTemplateCourseUpdate extends TransferCreditTemplateCourseComplete {

  public void setTransferCreditTemplate(TransferCreditTemplateCompact transferCreditTemplate) {
    super.setTransferCreditTemplate(transferCreditTemplate);
  }

  public TransferCreditTemplateCompact getTransferCreditTemplate() {
    return (TransferCreditTemplateCompact)super.getTransferCreditTemplate();
  }

  public void setCourseLength(EducationalLengthCompact courseLength) {
    super.setCourseLength(courseLength);
  }

  public EducationalLengthCompact getCourseLength() {
    return (EducationalLengthCompact)super.getCourseLength();
  }

  public void setSubject(SubjectCompact subject) {
    super.setSubject(subject);
  }

  public SubjectCompact getSubject() {
    return (SubjectCompact)super.getSubject();
  }

  public final static String[] properties = {"transferCreditTemplate","courseLength","subject"};
}
