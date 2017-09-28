package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

public class LukionOppiaineenSuoritusMuuValtakunnallinen extends LukionOppiaineenTunniste {

  public LukionOppiaineenSuoritusMuuValtakunnallinen(KoskiOppiaineetYleissivistava tunniste, boolean pakollinen) {
    super(pakollinen);
    this.tunniste.setValue(tunniste);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>();
}
