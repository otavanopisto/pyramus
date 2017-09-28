package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusVierasKieli(KoskiOppiaineetYleissivistava tunniste, Kielivalikoima kieli, boolean pakollinen) {
    super(pakollinen);
    this.tunniste.setValue(tunniste);
    this.kieli.setValue(kieli);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  public KoodistoViite<Kielivalikoima> getKieli() {
    return kieli;
  }

  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>();
  private final KoodistoViite<Kielivalikoima> kieli = new KoodistoViite<>();
}
