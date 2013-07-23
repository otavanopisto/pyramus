package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplate.class, entityType = TranquilModelType.UPDATE)
public class TransferCreditTemplateUpdate extends TransferCreditTemplateComplete {

  public void setCourses(java.util.List<TransferCreditTemplateCourseCompact> courses) {
    super.setCourses(courses);
  }

  public java.util.List<TransferCreditTemplateCourseCompact> getCourses() {
    return (java.util.List<TransferCreditTemplateCourseCompact>)super.getCourses();
  }

  public final static String[] properties = {"courses"};
}
