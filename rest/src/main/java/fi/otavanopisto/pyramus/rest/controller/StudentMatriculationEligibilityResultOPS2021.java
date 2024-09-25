package fi.otavanopisto.pyramus.rest.controller;

public class StudentMatriculationEligibilityResultOPS2021 {

  /**
   * Constructor
   * 
   * @param requiredPassingGradeCourseCreditPoints number of required course credit
   * @param passingGradeCourseCreditPoints sum of passed course credit points the student has
   * @param eligible whether student is eligible to participate matriculation exam
   */
  public StudentMatriculationEligibilityResultOPS2021(Double requiredPassingGradeCourseCreditPoints, Double passingGradeCourseCreditPoints, boolean eligible) {
    this.requiredPassingGradeCourseCreditPoints = requiredPassingGradeCourseCreditPoints;
    this.passingGradeCourseCreditPoints = passingGradeCourseCreditPoints;
    this.eligible = eligible;
  }
  
  public Double getRequiredPassingGradeCourseCreditPoints() {
    return requiredPassingGradeCourseCreditPoints;
  }

  public Double getPassingGradeCourseCreditPoints() {
    return passingGradeCourseCreditPoints;
  }

  public boolean isEligible() {
    return eligible;
  }

  private final Double requiredPassingGradeCourseCreditPoints;
  private final Double passingGradeCourseCreditPoints;
  private final boolean eligible;
}
