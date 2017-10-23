package fi.otavanopisto.pyramus.koski.koodisto;

import fi.otavanopisto.pyramus.koski.KoodistoEnum;

@KoodistoEnum("kunta")
public enum Kunta {

  K491 (491);
  
  private Kunta(int value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
  
  private int value;
}
