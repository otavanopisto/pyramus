package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("arviointiasteikkoyleissivistava")
public enum ArviointiasteikkoYleissivistava {
  
  GRADE_4 ("4"),   // hylätty
  GRADE_5 ("5"),   // välttävä
  GRADE_6 ("6"),   // kohtalainen
  GRADE_7 ("7"),   // tyydyttävä
  GRADE_8 ("8"),   // hyvä
  GRADE_9 ("9"),   // kiitettävä
  GRADE_10 ("10"), // erinomainen
  GRADE_H ("H"),   // hylätty
  GRADE_S ("S");    // hyväksytty
  
  ArviointiasteikkoYleissivistava(String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  private String value;
}
