package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("koulutus")
public enum Koulutus {
  /* Incomplete list */

  K201101 ("201101"),
  K309902 ("309902");
  
  Koulutus(String value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return value;
  }
  
  private String value;
}
