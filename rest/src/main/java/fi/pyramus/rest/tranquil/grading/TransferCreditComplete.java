package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;
import fi.tranquil.TranquilModelEntity;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCredit.class, entityType = TranquilModelType.COMPLETE)
public class TransferCreditComplete extends TransferCreditBase {

  public TranquilModelEntity getGrade() {
    return grade;
  }

  public void setGrade(TranquilModelEntity grade) {
    this.grade = grade;
  }

  public TranquilModelEntity getAssessingUser() {
    return assessingUser;
  }

  public void setAssessingUser(TranquilModelEntity assessingUser) {
    this.assessingUser = assessingUser;
  }

  public TranquilModelEntity getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(TranquilModelEntity courseLength) {
    this.courseLength = courseLength;
  }

  public TranquilModelEntity getSchool() {
    return school;
  }

  public void setSchool(TranquilModelEntity school) {
    this.school = school;
  }

  public TranquilModelEntity getSubject() {
    return subject;
  }

  public void setSubject(TranquilModelEntity subject) {
    this.subject = subject;
  }

  public TranquilModelEntity getStudent() {
    return student;
  }

  public void setStudent(TranquilModelEntity student) {
    this.student = student;
  }

  private TranquilModelEntity grade;

  private TranquilModelEntity assessingUser;

  private TranquilModelEntity courseLength;

  private TranquilModelEntity school;

  private TranquilModelEntity subject;

  private TranquilModelEntity student;

  public final static String[] properties = {"grade","assessingUser","courseLength","school","subject","student"};
}
