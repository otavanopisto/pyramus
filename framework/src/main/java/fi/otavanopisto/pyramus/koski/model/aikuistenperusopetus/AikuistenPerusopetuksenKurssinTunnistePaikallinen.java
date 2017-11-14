package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class AikuistenPerusopetuksenKurssinTunnistePaikallinen extends AikuistenPerusopetuksenKurssinTunniste {

  public AikuistenPerusopetuksenKurssinTunnistePaikallinen(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  private final PaikallinenKoodi tunniste;
}
