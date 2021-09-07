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

  private int numberCompletedCourses;
  private double numberCreditPoints;
}
