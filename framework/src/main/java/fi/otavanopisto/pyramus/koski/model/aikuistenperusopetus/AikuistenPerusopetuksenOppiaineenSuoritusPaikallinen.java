package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

public class AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(PaikallinenKoodi tunniste, boolean pakollinen, Kuvaus kuvaus) {
    super(pakollinen);
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
