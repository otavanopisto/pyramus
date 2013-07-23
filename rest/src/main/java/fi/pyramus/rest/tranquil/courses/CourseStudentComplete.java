package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseStudent.class, entityType = TranquilModelType.COMPLETE)
public class CourseStudentComplete extends CourseStudentBase {

  public TranquilModelEntity getCourse() {
    return course;
  }

  public void setCourse(TranquilModelEntity course) {
    this.course = course;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  public TranquilModelEntity getParticipationType() {
    return participationType;
  }

  public void setParticipationType(TranquilModelEntity participationType) {
    this.participationType = participationType;
  }

  public TranquilModelEntity getCourseEnrolmentType() {
    return courseEnrolmentType;
  }

  public void setCourseEnrolmentType(TranquilModelEntity courseEnrolmentType) {
    this.courseEnrolmentType = courseEnrolmentType;
  }

  public TranquilModelEntity getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(TranquilModelEntity billingDetails) {
    this.billingDetails = billingDetails;
  }

  private TranquilModelEntity course;

  private TranquilModelEntity student;

  private TranquilModelEntity participationType;

  private TranquilModelEntity courseEnrolmentType;

  private TranquilModelEntity billingDetails;

  public final static String[] properties = {"course","student","participationType","courseEnrolmentType","billingDetails"};
}
