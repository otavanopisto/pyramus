package fi.pyramus.rest.tranquil.courses;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.courses.CourseStudent.class, entityType = TranquilModelType.UPDATE)
public class CourseStudentUpdate extends CourseStudentComplete {

  public void setCourse(CourseCompact course) {
    super.setCourse(course);
  }

  public CourseCompact getCourse() {
    return (CourseCompact)super.getCourse();
  }

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public void setParticipationType(CourseParticipationTypeCompact participationType) {
    super.setParticipationType(participationType);
  }

  public CourseParticipationTypeCompact getParticipationType() {
    return (CourseParticipationTypeCompact)super.getParticipationType();
  }

  public void setCourseEnrolmentType(CourseEnrolmentTypeCompact courseEnrolmentType) {
    super.setCourseEnrolmentType(courseEnrolmentType);
  }

  public CourseEnrolmentTypeCompact getCourseEnrolmentType() {
    return (CourseEnrolmentTypeCompact)super.getCourseEnrolmentType();
  }

  public void setBillingDetails(BillingDetailsCompact billingDetails) {
    super.setBillingDetails(billingDetails);
  }

  public BillingDetailsCompact getBillingDetails() {
    return (BillingDetailsCompact)super.getBillingDetails();
  }

  public final static String[] properties = {"course","student","participationType","courseEnrolmentType","billingDetails"};
}
