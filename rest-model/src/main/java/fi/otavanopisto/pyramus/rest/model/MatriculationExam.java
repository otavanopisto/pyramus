package fi.otavanopisto.pyramus.rest.model;

public class MatriculationExam {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getStarts() {
    return starts;
  }

  public void setStarts(Long starts) {
    this.starts = starts;
  }

  public Long getEnds() {
    return ends;
  }

  public void setEnds(Long ends) {
    this.ends = ends;
  }

  public boolean isEligible() {
    return eligible;
  }

  public void setEligible(boolean eligible) {
    this.eligible = eligible;
  }

  public boolean isEnrolled() {
    return enrolled;
  }

  public void setEnrolled(boolean enrolled) {
    this.enrolled = enrolled;
  }

  public Long getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(Long enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }

  private Long id;
  private Long starts;
  private Long ends;
  private boolean eligible;
  private boolean enrolled;
  private Long enrollmentDate;
}

