package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class LukionOppiaineenSuoritusAidinkieli extends LukionOppiaineenTunniste {

  public LukionOppiaineenSuoritusAidinkieli(OppiaineAidinkieliJaKirjallisuus kieli, boolean pakollinen) {
    super(pakollinen);
    this.kieli.setValue(kieli);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  public KoodistoViite<OppiaineAidinkieliJaKirjallisuus> getKieli() {
    return kieli;
  }

  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>(KoskiOppiaineetYleissivistava.AI);
  private final KoodistoViite<OppiaineAidinkieliJaKirjallisuus> kieli = new KoodistoViite<>();
}
