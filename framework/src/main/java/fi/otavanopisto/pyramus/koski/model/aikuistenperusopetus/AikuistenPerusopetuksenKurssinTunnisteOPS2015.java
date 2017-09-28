package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class AikuistenPerusopetuksenKurssinTunnisteOPS2015 extends AikuistenPerusopetuksenKurssinTunniste {

  public AikuistenPerusopetuksenKurssinTunnisteOPS2015(AikuistenPerusopetuksenKurssit2015 tunniste) {
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<AikuistenPerusopetuksenKurssit2015> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<AikuistenPerusopetuksenKurssit2015> tunniste = new KoodistoViite<>();
}
