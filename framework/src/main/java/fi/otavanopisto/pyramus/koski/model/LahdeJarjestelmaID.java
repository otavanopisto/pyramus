package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Lahdejarjestelma;

public class LahdeJarjestelmaID {

  public LahdeJarjestelmaID() {
  }
  
  public LahdeJarjestelmaID(String id, Lahdejarjestelma lahdejarjestelma) {
    this.id = id;
    this.lahdejarjestelma.setValue(lahdejarjestelma);
  }
  
  @JsonProperty("lähdejärjestelmä")
  public KoodistoViite<Lahdejarjestelma> getLahdejarjestelma() {
    return lahdejarjestelma;
  }
  
  public void setLahdejarjestelma(KoodistoViite<Lahdejarjestelma> lahdejarjestelma) {
    this.lahdejarjestelma = lahdejarjestelma;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  private String id;
  private KoodistoViite<Lahdejarjestelma> lahdejarjestelma = new KoodistoViite<>();
}
