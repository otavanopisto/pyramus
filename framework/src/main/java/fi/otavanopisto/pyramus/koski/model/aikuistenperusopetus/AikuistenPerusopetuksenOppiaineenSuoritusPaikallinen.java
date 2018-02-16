package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(PaikallinenKoodi tunniste, boolean pakollinen, Kuvaus kuvaus) {
    super();
    this.tunniste = tunniste;
    this.pakollinen = pakollinen;
    this.kuvaus = kuvaus;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  public boolean getPakollinen() {
    return pakollinen;
  }

  private final PaikallinenKoodi tunniste;
  private final Kuvaus kuvaus;
  private final boolean pakollinen;
}
