package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoEnum;

@KoodistoEnum("opintojenlaajuusyksikko")
public enum OpintojenLaajuusYksikko {

  ov (1),
  op (2),
  vuosiviikkotuntia (3),
  kurssia (4),
  tuntia (5),
  osp (6),
  vuotta (7);
  
  OpintojenLaajuusYksikko(int value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
  
  private int value;
}
