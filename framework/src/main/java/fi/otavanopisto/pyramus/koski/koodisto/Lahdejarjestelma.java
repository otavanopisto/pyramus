package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("lahdejarjestelma")
public enum Lahdejarjestelma {
  
  edukit ("edukit"),
  helmi ("helmi"),
  msdynamicscrm_aes ("msdynamicscrm_aes"),
  nexttime ("nexttime"),
  peppi ("peppi"),
  primus ("primus"),
  rediteq ("rediteq"),
  studentaplus ("studentaplus"),
  virta ("virta"),
  winha ("winha"),
  winnova ("winnova"),
  winnova_eduerp ("winnova-eduerp"),
  winnova_studentaplus ("winnova-studentaplus"),
  ytr ("ytr");
  
  Lahdejarjestelma(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
  
  private String value;
}
