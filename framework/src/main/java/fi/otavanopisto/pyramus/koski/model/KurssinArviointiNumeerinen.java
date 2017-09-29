package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiNumeerinen extends KurssinArviointi {

  public KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    this.arvosana.setValue(arvosana);
    this.paiva = paiva;
  }
  
  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }
  
  @JsonProperty("päivä")
  public Date getPaiva() {
    return paiva;
  }
  
  private final KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
  private final Date paiva;
}