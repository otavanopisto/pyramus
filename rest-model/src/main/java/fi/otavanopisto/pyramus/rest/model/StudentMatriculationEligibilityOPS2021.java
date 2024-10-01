package fi.otavanopisto.pyramus.rest.model;

public class StudentMatriculationEligibilityOPS2021 {

  public StudentMatriculationEligibilityOPS2021() {
  }
  
  public StudentMatriculationEligibilityOPS2021(boolean eligible, Double requiredPassingGradeCourseCreditPoints,
      Double passingGradeCourseCreditPoints) {
    this.eligible = eligible;
    this.requiredPassingGradeCourseCreditPoints = requiredPassingGradeCourseCreditPoints;
    this.passingGradeCourseCreditPoints = passingGradeCourseCreditPoints;
  }

  public boolean isEligible() {
    return eligible;
  }

  public Double getRequiredPassingGradeCourseCreditPoints() {
    return requiredPassingGradeCourseCreditPoints;
  }

  public Double getPassingGradeCourseCreditPoints() {
    return passingGradeCourseCreditPoints;
  }

  private boolean eligible;
  private Double requiredPassingGradeCourseCreditPoints;
  private Double passingGradeCourseCreditPoints;
}
