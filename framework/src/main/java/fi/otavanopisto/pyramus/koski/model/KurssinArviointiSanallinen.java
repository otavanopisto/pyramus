package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class KurssinArviointiSanallinen extends KurssinArviointi {

  public KurssinArviointiSanallinen() {
  }
  
  public KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava arvosana, Date paiva, Kuvaus kuvaus) {
    super(arvosana, paiva);
    this.kuvaus = kuvaus;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  public void setKuvaus(Kuvaus kuvaus) {
    this.kuvaus = kuvaus;
  }

  private Kuvaus kuvaus;
}
