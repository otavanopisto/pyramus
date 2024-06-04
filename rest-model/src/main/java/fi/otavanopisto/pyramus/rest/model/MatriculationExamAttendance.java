package fi.otavanopisto.pyramus.rest.model;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceFunding;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

public class MatriculationExamAttendance {

  public Long getEnrollmentId() {
    return enrollmentId;
  }

  public void setEnrollmentId(Long enrollmentId) {
    this.enrollmentId = enrollmentId;
  }

  public MatriculationExamSubject getSubject() {
    return subject;
  }

  public void setSubject(MatriculationExamSubject subject) {
    this.subject = subject;
  }

  public Boolean getMandatory() {
    return mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Boolean getRepeat() {
    return repeat;
  }

  public void setRepeat(Boolean repeat) {
    this.repeat = repeat;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public MatriculationExamTerm getTerm() {
    return term;
  }

  public void setTerm(MatriculationExamTerm term) {
    this.term = term;
  }

  public MatriculationExamAttendanceStatus getStatus() {
    return status;
  }

  public void setStatus(MatriculationExamAttendanceStatus status) {
    this.status = status;
  }

  public MatriculationExamGrade getGrade() {
    return grade;
  }

  public void setGrade(MatriculationExamGrade grade) {
    this.grade = grade;
  }

  public MatriculationExamAttendanceFunding getFunding() {
    return funding;
  }

  public void setFunding(MatriculationExamAttendanceFunding funding) {
    this.funding = funding;
  }

  private Long enrollmentId;
  private MatriculationExamSubject subject;
  private Boolean mandatory;
  private Boolean repeat;
  private Integer year;
  private MatriculationExamTerm term;
  private MatriculationExamAttendanceStatus status;
  private MatriculationExamAttendanceFunding funding;
  private MatriculationExamGrade grade;

}

