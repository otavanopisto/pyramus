package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.grading.TransferCredit.class, entityType = TranquilModelType.UPDATE)
public class TransferCreditUpdate extends TransferCreditComplete {

  public void setGrade(GradeCompact grade) {
    super.setGrade(grade);
  }

  public GradeCompact getGrade() {
    return (GradeCompact)super.getGrade();
  }

  public void setAssessingUser(UserCompact assessingUser) {
    super.setAssessingUser(assessingUser);
  }

  public UserCompact getAssessingUser() {
    return (UserCompact)super.getAssessingUser();
  }

  public void setCourseLength(EducationalLengthCompact courseLength) {
    super.setCourseLength(courseLength);
  }

  public EducationalLengthCompact getCourseLength() {
    return (EducationalLengthCompact)super.getCourseLength();
  }

  public void setSchool(SchoolCompact school) {
    super.setSchool(school);
  }

  public SchoolCompact getSchool() {
    return (SchoolCompact)super.getSchool();
  }

  public void setSubject(SubjectCompact subject) {
    super.setSubject(subject);
  }

  public SubjectCompact getSubject() {
    return (SubjectCompact)super.getSubject();
  }

  public void setStudent(StudentCompact student) {
    super.setStudent(student);
  }

  public StudentCompact getStudent() {
    return (StudentCompact)super.getStudent();
  }

  public final static String[] properties = {"grade","assessingUser","courseLength","school","subject","student"};
}
