package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class Laajuus {

  public Laajuus(int arvo, OpintojenLaajuusYksikko yksikko) {
    this.arvo = arvo;
    this.yksikko.setValue(yksikko);
  }

  public int getArvo() {
    return arvo;
  }
  
  @JsonProperty("yksikkö")
  public KoodistoViite<OpintojenLaajuusYksikko> getYksikko() {
    return yksikko;
  }
  
  private final int arvo;

  private final KoodistoViite<OpintojenLaajuusYksikko> yksikko = new KoodistoViite<>();
}
