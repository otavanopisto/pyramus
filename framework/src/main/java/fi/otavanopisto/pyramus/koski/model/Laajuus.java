package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;

public class Laajuus {

  public Laajuus() {
  }
  
  public Laajuus(double arvo, OpintojenLaajuusYksikko yksikko) {
    this.arvo = arvo;
    this.yksikko.setValue(yksikko);
  }

  public double getArvo() {
    return arvo;
  }
  
  @JsonProperty("yksikkö")
  public KoodistoViite<OpintojenLaajuusYksikko> getYksikko() {
    return yksikko;
  }
  
  private double arvo;
  private final KoodistoViite<OpintojenLaajuusYksikko> yksikko = new KoodistoViite<>();
}
