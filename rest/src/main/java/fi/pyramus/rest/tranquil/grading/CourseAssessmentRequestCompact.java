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

  public Long getStudent_id() {
    return student_id;
  }

  public void setStudent_id(Long student_id) {
    this.student_id = student_id;
  }

  private Long courseStudent_id;

  private Long student_id;

  public final static String[] properties = {"courseStudent","student"};
}
