package fi.otavanopisto.pyramus.rest.model;

public class StudentCourseStats {

  public int getNumberCompletedCourses() {
    return numberCompletedCourses;
  }

  public void setNumberCompletedCourses(int numberCompletedCourses) {
    this.numberCompletedCourses = numberCompletedCourses;
  }

  public double getNumberCreditPoints() {
    return numberCreditPoints;
  }

  public void setNumberCreditPoints(double numberCreditPoints) {
    this.numberCreditPoints = numberCreditPoints;
  }

  public boolean isPersonHasCourseAssessments() {
    return personHasCourseAssessments;
  }

  public void setPersonHasCourseAssessments(boolean personHasCourseAssessments) {
    this.personHasCourseAssessments = personHasCourseAssessments;
  }

  private int numberCompletedCourses;
  private double numberCreditPoints;
  private boolean personHasCourseAssessments;
}
