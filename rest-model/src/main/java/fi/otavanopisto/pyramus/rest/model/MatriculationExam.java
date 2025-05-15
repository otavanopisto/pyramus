package fi.otavanopisto.pyramus.rest.model;

import java.time.LocalDate;

import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

public class MatriculationExam {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getStarts() {
    return starts;
  }

  public void setStarts(LocalDate starts) {
    this.starts = starts;
  }

  public LocalDate getEnds() {
    return ends;
  }

  public void setEnds(LocalDate ends) {
    this.ends = ends;
  }

  public boolean isCompulsoryEducationEligible() {
    return compulsoryEducationEligible;
  }

  public void setCompulsoryEducationEligible(boolean compulsoryEducationEligible) {
    this.compulsoryEducationEligible = compulsoryEducationEligible;
  }

  public MatriculationExamStudentStatus getStudentStatus() {
    return studentStatus;
  }

  public void setStudentStatus(MatriculationExamStudentStatus studentStatus) {
    this.studentStatus = studentStatus;
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

  public MatriculationExamEnrollment getEnrollment() {
    return enrollment;
  }

  public void setEnrollment(MatriculationExamEnrollment enrollment) {
    this.enrollment = enrollment;
  }

  public LocalDate getConfirmDate() {
    return confirmDate;
  }

  public void setConfirmDate(LocalDate confirmDate) {
    this.confirmDate = confirmDate;
  }

  private Long id;
  private Integer year;
  private MatriculationExamTerm term;
  private LocalDate starts;
  private LocalDate ends;
  private LocalDate confirmDate;
  private boolean compulsoryEducationEligible;
  private MatriculationExamStudentStatus studentStatus;
  private MatriculationExamEnrollment enrollment;
}
