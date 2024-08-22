package fi.otavanopisto.pyramus.rest.model;

/**
 * REST model for student matriculation eligibility query
 * 
 * @author Antti Leppä
 */
@Deprecated
public class StudentMatriculationEligibility {

  private Boolean eligible;
  private int requirePassingGrades;
  private int acceptedCourseCount;
  private int acceptedTransferCreditCount;

  /**
   * Constructor
   */
  public StudentMatriculationEligibility() {
    // Zero-argument constructor
  }
  
  /**
   * Constructor
   * 
   * @param requirePassingGrades number of required passing grades
   * @param acceptedCourseCount accepted course count
   * @param acceptedTransferCreditCount accepted transfer credit count
   * @param eligible whether student is eligible to participate matriculation exam
   */
  public StudentMatriculationEligibility(Boolean eligible, int requirePassingGrades, int acceptedCourseCount, int acceptedTransferCreditCount) {
    super();
    this.eligible = eligible;
    this.requirePassingGrades = requirePassingGrades;
    this.acceptedCourseCount = acceptedCourseCount;
    this.acceptedTransferCreditCount = acceptedTransferCreditCount;
  }

  /**
   * Returns whether student is eligible to participate matriculation exam
   * 
   * @return whether student is eligible to participate matriculation exam
   */
  public Boolean getEligible() {
    return eligible;
  }
  
  /**
   * Sets whether student is eligible to participate matriculation exam
   * 
   * @param eligible whether student is eligible to participate matriculation exam
   */
  public void setEligible(Boolean eligible) {
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
   * Sets the number of required passing grades
   * 
   * @param requirePassingGrades the number of required passing grades
   */
  public void setRequirePassingGrades(int requirePassingGrades) {
    this.requirePassingGrades = requirePassingGrades;
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
   * Sets accepted course count
   * 
   * @param acceptedCourseCount accepted course count
   */
  public void setAcceptedCourseCount(int acceptedCourseCount) {
    this.acceptedCourseCount = acceptedCourseCount;
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
   * Sets accepted transfer credit count
   * 
   * @param acceptedTransferCreditCount accepted transfer credit count
   */
  public void setAcceptedTransferCreditCount(int acceptedTransferCreditCount) {
    this.acceptedTransferCreditCount = acceptedTransferCreditCount;
  }

}
