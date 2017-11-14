package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

public class AikuistenPerusopetuksenOppiaineenSuoritusMuu extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusMuu(KoskiOppiaineetYleissivistava tunniste, boolean pakollinen) {
    super(pakollinen);
    this.tunniste.setValue(tunniste);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>();
}
