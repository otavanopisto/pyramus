package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

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
