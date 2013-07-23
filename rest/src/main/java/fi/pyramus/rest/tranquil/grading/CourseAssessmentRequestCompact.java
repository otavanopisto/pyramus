package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.CourseAssessmentRequest.class, entityType = TranquilModelType.COMPACT)
public class CourseAssessmentRequestCompact extends CourseAssessmentRequestBase {

  public Long getCourseStudent_id() {
    return courseStudent_id;
  }

  public void setCourseStudent_id(Long courseStudent_id) {
    this.courseStudent_id = courseStudent_id;
  }

  private Long courseStudent_id;

  public final static String[] properties = {"courseStudent"};
}
