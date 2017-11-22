package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class APAKurssinTunnistePaikallinen extends APAKurssinTunniste {

  public APAKurssinTunnistePaikallinen(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  private final PaikallinenKoodi tunniste;
}
