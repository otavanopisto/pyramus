package fi.otavanopisto.pyramus.views.students.tor;

import java.util.Date;

public class TORCredit {

  public TORCredit() {
  }
  
  public TORCredit(Long gradeId, String gradeName, Double numericGrade, Date date, TORCreditType creditType, Boolean passingGrade) {
    this.gradeId = gradeId;
    this.gradeName = gradeName;
    this.numericGrade = numericGrade;
    this.date = date;
    this.creditType = creditType;
    this.passingGrade = passingGrade;
  }
  
  public TORCreditType getCreditType() {
    return creditType;
  }

  public void setCreditType(TORCreditType creditType) {
    this.creditType = creditType;
  }

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public Double getNumericGrade() {
    return numericGrade;
  }

  public void setNumericGrade(Double numericGrade) {
    this.numericGrade = numericGrade;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  private Long gradeId;
  private String gradeName;
  private Double numericGrade;
  private TORCreditType creditType;
  private Date date;
  private Boolean passingGrade;
}
