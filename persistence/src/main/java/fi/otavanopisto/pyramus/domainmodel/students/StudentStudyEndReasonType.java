package fi.otavanopisto.pyramus.domainmodel.students;

public enum StudentStudyEndReasonType {

  /**
   * The student has completed the studies and is considered graduated
   */
  GRADUATED,
  
  /**
   * The student has resigned by their own notification
   */
  RESIGNED,
  
  /**
   * The student has been dismissed by the decision of the school
   */
  DISMISSED
}
