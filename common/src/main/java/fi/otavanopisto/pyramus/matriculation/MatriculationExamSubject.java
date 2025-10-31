package fi.otavanopisto.pyramus.matriculation;

import java.util.EnumSet;

/**
 * YTL:n lista yo-kirjoituksissa kirjoitettavista oppiaineista (03/2025):
 * https://github.com/digabi/ilmoittautuminen/blob/master/koekoodit_ja_nimet.csv
 * 
 * TODO Ainekoodit saisi olla samat kuin YTL:n listassa, nyt vähän sekasikiö ainekoodeista ja YTL:n koodeista.
 */
public enum MatriculationExamSubject {

  AI,
  S2,
  O,            // Äidinkieli ja kirjallisuus, ruotsi (ei käytössä)
  O5,           // Äidinkieli ja kirjallisuus, ruotsi toisena kielenä
  CA,           // Suomi, pitkä oppimäärä (ei käytössä)
  CB,           // Suomi, keskipitkä oppimäärä (ei käytössä)
  ENA,
  RAA,
  ESA,
  SAA,
  VEA,
  UE,
  UO,
  ET,
  YO,
  KE,
  GE,
  TT,
  ENC,
  RAC,
  ESC,
  SAC,
  VEC,
  ITC,
  POC,
  LAC,          // Latina, lyhyt oppimäärä - TODO YTL-koodi on L1
  L7,           // Latina, laajempi lyhyt oppimäärä
  SMC,
  SM_DC,
  SM_ICC,
  SM_QC,
  RUA,
  RUB,
  PS,
  FI,
  HI,
  FY,
  BI,
  MAA,
  MAB,
  I,            // Äidinkieli ja kirjallisuus, inarinsaame
  W,            // Äidinkieli ja kirjallisuus, koltansaame
  Z;            // Äidinkieli ja kirjallisuus, pohjoissaame

  /**
   * Äidinkieli ja kirjallisuus -oppiaineet
   */
  public static final EnumSet<MatriculationExamSubject> ÄIDINKIELI_SUBJECTS = EnumSet.of(
      MatriculationExamSubject.AI, 
      MatriculationExamSubject.S2, 
      MatriculationExamSubject.I, 
      MatriculationExamSubject.W, 
      MatriculationExamSubject.Z
  );

  public boolean isÄidinkieli() {
    return ÄIDINKIELI_SUBJECTS.contains(this);
  }
  
}
