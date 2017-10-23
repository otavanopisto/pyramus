package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiSanallinen extends KurssinArviointi {
  
  public KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava arvosana, Date paivamaara, Kuvaus kuvaus) {
    super(arvosana);
    this.paivamaara = paivamaara;
    this.kuvaus = kuvaus;
  }
  
  @JsonProperty("päivämäärä")
  public Date getPaivamaara() {
    return paivamaara;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  private final Kuvaus kuvaus;
  private final Date paivamaara;
}
