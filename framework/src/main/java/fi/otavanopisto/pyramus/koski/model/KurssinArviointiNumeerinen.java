package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiNumeerinen extends KurssinArviointi {

  public KurssinArviointiNumeerinen(ArviointiasteikkoYleissivistava arvosana, Date paiva) {
    super(arvosana);
    this.paiva = paiva;
  }
  
  @JsonProperty("päivä")
  public Date getPaiva() {
    return paiva;
  }
  
  private final Date paiva;
}
