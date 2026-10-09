package fi.otavanopisto.pyramus.rest.model.report;

public enum KoskiCreditError {
  
  // Vastaavaa arviointia ei löytynyt
  NOT_FOUND,
  
  // Löytyi useita vastaavia arviointeja
  FOUND_MULTIPLE_MATCHING_CREDITS,
  
  // Tunnustettu-tieto puuttuu taulukosta
  NULL_TUNNUSTETTU,
  
  // Pyramuksen kurssiarviointi on taulukossa tunnustettu
  COURSEASSESSMENT_MARKEDAS_TRANSFERCREDIT,
  
  // Pyramuksen hyväksiluku on taulukossa kurssiarviointi
  TRANSFERCREDIT_MARKEDAS_COURSEASSESSMENT,
  
  // Hyväksiluvun rahoitusmerkintä on väärin
  TRANSFERCREDIT_WRONG_FUNDING,
  
  // Koski: ensimmäinen arviointi, Pyramus: löytyy edeltäviä arviointeja
  FIRSTASSESSMENT_HASPREVIOUSASSESSMENTS,

  // Koski: ei ensimmäinen arviointi, Pyramus: ei edeltäviä arviointeja
  REPEATASSESSMENT_NOPREVIOUSASSESSMENTS,
  
  // Hylätyn korotus eri arvo kuin Pyramuksesta tulkittu
  REPEATASSESSMENT_WRONG_RAISED_FROM_NONPASSING,
  
  // Hyväksytyn korotus eri arvo kuin Pyramuksesta tulkittu
  REPEATASSESSMENT_WRONG_RAISED_FROM_PASSING
}
