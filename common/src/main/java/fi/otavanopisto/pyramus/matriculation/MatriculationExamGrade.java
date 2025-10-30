package fi.otavanopisto.pyramus.matriculation;

public enum MatriculationExamGrade {
  IMPROBATUR,
  APPROBATUR,
  LUBENTER_APPROBATUR,
  CUM_LAUDE_APPROBATUR,
  MAGNA_CUM_LAUDE_APPROBATUR,
  EXIMIA_CUM_LAUDE_APPROBATUR,
  LAUDATUR,
  UNKNOWN,
  NO_RIGHT_TO_PARTICIPATE,
  INVALIDATED,
  K;
  

  /**
   * Returns MatriculationExamGrade that represents the grade in YTL.
   * Otherwise returns null.
   * 
   * @param ytlGrade The one-letter grade code used by YTL
   * @return
   */
  public static MatriculationExamGrade fromYTLGrade(String ytlGrade) {
    if (ytlGrade == null) {
      throw new IllegalArgumentException();
    }
    
    switch (ytlGrade) {
      case "L":
        return LAUDATUR;
      case "E":
        return EXIMIA_CUM_LAUDE_APPROBATUR;
      case "M":
        return MAGNA_CUM_LAUDE_APPROBATUR;
      case "C":
        return CUM_LAUDE_APPROBATUR;
      case "B":
        return LUBENTER_APPROBATUR;
      case "A":
        return APPROBATUR;
      case "I":
        return IMPROBATUR;
      case "K":
        return K;
    }
    
    return null;
  }
  
}
