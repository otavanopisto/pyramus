package fi.otavanopisto.pyramus.tor;

public enum TORProblemType {
  
  /**
   * Course length units from two or more sources didn't match
   */
  INCOMPATIBLE_LENGTHUNITS, 
  
  /**
   * Credit mandatorities from two or more sources didn't match
   */
  INCOMPATIBLE_MANDATORITIES, 
  
  /**
   * Length unit could not be resolved
   */
  UNRESOLVABLE_LENGTHUNIT

}
