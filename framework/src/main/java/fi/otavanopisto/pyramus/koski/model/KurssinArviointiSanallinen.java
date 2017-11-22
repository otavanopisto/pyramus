package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.ArviointiasteikkoYleissivistava;

public class KurssinArviointiSanallinen extends KurssinArviointi {
  
  public KurssinArviointiSanallinen(ArviointiasteikkoYleissivistava arvosana, Date paiva, Kuvaus kuvaus) {
    super(arvosana, paiva);
    this.kuvaus = kuvaus;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  private final Kuvaus kuvaus;
}
