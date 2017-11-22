package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenKurssit2017;

public class APAKurssinTunnisteOPS2017 extends APAKurssinTunniste {

  public APAKurssinTunnisteOPS2017(AikuistenPerusopetuksenAlkuvaiheenKurssit2017 tunniste) {
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenKurssit2017> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenKurssit2017> tunniste = new KoodistoViite<>();
}
