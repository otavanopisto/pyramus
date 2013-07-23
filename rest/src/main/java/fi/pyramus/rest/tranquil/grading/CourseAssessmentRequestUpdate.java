package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.CourseAssessmentRequest.class, entityType = TranquilModelType.UPDATE)
public class CourseAssessmentRequestUpdate extends CourseAssessmentRequestComplete {

  public void setCourseStudent(CourseStudentCompact courseStudent) {
    super.setCourseStudent(courseStudent);
  }

  public CourseStudentCompact getCourseStudent() {
    return (CourseStudentCompact)super.getCourseStudent();
  }

  public final static String[] properties = {"courseStudent"};
}
