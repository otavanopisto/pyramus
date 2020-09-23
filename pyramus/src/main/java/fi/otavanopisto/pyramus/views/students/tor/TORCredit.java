package fi.otavanopisto.pyramus.views.students.tor;

import java.util.Date;

public class TORCredit implements Comparable<TORCredit> {

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

  /**
   * Compares two TORCredits, returns negative values when this object is "less" than
   * the provided TORCredit and positive values if the provided value is "less" than
   * this object.
   * 
   * Comparison logic is as follows:
   * * numericGrades are compared
   * * passingGrades are compared
   * * dates are compared
   * 
   * Each step may stop the comparison if values exist for both and they are different. 
   * 0 is returned if all steps are passed and everything was equal until that point.
   * 
   * @param o
   * @return
   */
  @Override
  public int compareTo(TORCredit o) {
    int comp = 0;
    if ((numericGrade != null) && (o.getNumericGrade() != null)) {
      comp = Double.compare(numericGrade, o.getNumericGrade());
      if (comp != 0) {
        // Numeric grades differ
        return comp;
      }
    } else {
      if ((numericGrade == null) && (o.getNumericGrade() != null)) {
        return -1;
      }
      if ((numericGrade != null) && (o.getNumericGrade() == null)) {
        return 1;
      }
    }
    
    if (Boolean.FALSE.equals(passingGrade) && Boolean.TRUE.equals(o.getPassingGrade())) {
      return -1;
    }
    
    if (Boolean.TRUE.equals(passingGrade) && Boolean.FALSE.equals(o.getPassingGrade())) {
      return 1;
    }
    
    if ((date != null) && (o.getDate() != null)) {
      return date.compareTo(o.getDate());
    } else {
      if ((date == null) && (o.getDate() == null)) {
        return 0;
      } else {
        if (date == null) {
          return 1;
        } else {
          return -1;
        }
      }
    }
  }
  
  private Long gradeId;
  private String gradeName;
  private Double numericGrade;
  private TORCreditType creditType;
  private Date date;
  private Boolean passingGrade;
}
