package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CourseAssessmentRequest.class, entityType = TranquilModelType.COMPLETE)
public class CourseAssessmentRequestComplete extends CourseAssessmentRequestBase {

  public TranquilModelEntity getCourseStudent() {
    return courseStudent;
  }

  public void setCourseStudent(TranquilModelEntity courseStudent) {
    this.courseStudent = courseStudent;
  }

  private TranquilModelEntity courseStudent;

  public final static String[] properties = {"courseStudent"};
}
