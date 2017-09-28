package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiSanallinen extends KurssinArviointi {
  
  public KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava arvosana, Date paivamaara) {
    this.arvosana.setValue(arvosana);
    this.paivamaara = paivamaara;
  }
  
  public KoodistoViite<ArviointiasteikkoYleissivistava> getArvosana() {
    return arvosana;
  }
  
  @JsonProperty("päivämäärä")
  public Date getPaivamaara() {
    return paivamaara;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  private final KoodistoViite<ArviointiasteikkoYleissivistava> arvosana = new KoodistoViite<>();
  private final Kuvaus kuvaus = new Kuvaus();
  private final Date paivamaara;
}
