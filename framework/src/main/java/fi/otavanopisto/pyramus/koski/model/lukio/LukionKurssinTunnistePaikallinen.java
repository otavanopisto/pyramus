package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class LukionKurssinTunnistePaikallinen extends LukionKurssinTunniste {

  public LukionKurssinTunnistePaikallinen(PaikallinenKoodi tunniste, LukionKurssinTyyppi kurssinTyyppi, Kuvaus kuvaus) {
    super(kurssinTyyppi);
    this.tunniste = tunniste;
    this.kuvaus = kuvaus;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }
  
  private final PaikallinenKoodi tunniste;
  private final Kuvaus kuvaus;
}
