package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class APAOppiaineenTunnistePaikallinen extends APAOppiaineenTunniste {

  public APAOppiaineenTunnistePaikallinen(PaikallinenKoodi tunniste, Kuvaus kuvaus) {
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
