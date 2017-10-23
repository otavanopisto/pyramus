package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;

public class LahdeJarjestelmaID {

  public LahdeJarjestelmaID(String id, Lahdejarjestelma lahdejarjestelma) {
    this.id = id;
    this.lahdejarjestelma.setValue(lahdejarjestelma);
  }
  
  @JsonProperty("lähdejärjestelmä")
  public KoodistoViite<Lahdejarjestelma> getLahdejarjestelma() {
    return lahdejarjestelma;
  }
  
  public String getId() {
    return id;
  }

  private final String id;
  private final KoodistoViite<Lahdejarjestelma> lahdejarjestelma = new KoodistoViite<>();
}
