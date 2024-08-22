package fi.otavanopisto.pyramus.rest.controller;

/**
 * Result for student matriculation eligibility query
 * 
 * @author Antti Leppä
 */
@Deprecated
public class StudentMatriculationEligibilityResult {

  private int requirePassingGrades;
  private int acceptedCourseCount;
  private int acceptedTransferCreditCount;
  private boolean eligible;

  /**
   * Constructor
   * 
   * @param requirePassingGrades number of required passing grades
   * @param acceptedCourseCount accepted course count
   * @param acceptedTransferCreditCount accepted transfer credit count
   * @param eligible whether student is eligible to participate matriculation exam
   */
  public StudentMatriculationEligibilityResult(int requirePassingGrades, int acceptedCourseCount, int acceptedTransferCreditCount, boolean eligible) {
    this.requirePassingGrades = requirePassingGrades;
    this.acceptedCourseCount = acceptedCourseCount;
    this.acceptedTransferCreditCount = acceptedTransferCreditCount;
    this.eligible = eligible;
  }
  
  /**
   * Returns number of required passing grades
   * 
   * @return number of required passing grades
   */
  public int getRequirePassingGrades() {
    return requirePassingGrades;
  }
  
  /**
   * Returns accepted course count
   * 
   * @return accepted course count
   */
  public int getAcceptedCourseCount() {
    return acceptedCourseCount;
  }
  
  /**
   * Returns accepted transfer credit count
   * 
   * @return accepted transfer credit count
   */
  public int getAcceptedTransferCreditCount() {
    return acceptedTransferCreditCount;
  }
  
  /**
   * Returns whether student is eligible to participate matriculation exam
   * 
   * @return whether student is eligible to participate matriculation exam
   */
  public boolean getEligible() {
    return eligible;
  }

}
