package fi.otavanopisto.pyramus.matriculation;

public enum MatriculationExamEnrollmentFlag {

  // lisäaika kokeen suorittamiseen
  EXTENDED_EXAM_TIME,

  // erillinen pienryhmätila
  SEPARATE_GROUP_SPACE,
  
  // erillinen yksilötila
  SEPARATE_STUDIO,
  
  // lepäämiseen tarkoitettu tila
  RESTING_SPACE,
  
  // näkövammaisen koejärjestelyt
  VISION_IMPAIRED_SPACE,
  
  // oikeus suurentaa kirjasinkokoa
  PERMISSION_ENLARGED_FONT,
  
  // oikeus käyttää suurempaa näyttöä
  PERMISSION_BIGGER_MONITOR,
  
  // ääniaineistoltaan rajoitettu koe
  AUDIO_RESTRICTED_EXAM,
  
  // avustajan käyttö
  PERMISSION_ASSISTANT,
  
  // säädettävä työskentelyasento
  ADJUSTABLE_ERGONOMICS,
  
  // Muu tukitoimi
  OTHER_SUPPORTIVE_ELEMENT;
  
}
