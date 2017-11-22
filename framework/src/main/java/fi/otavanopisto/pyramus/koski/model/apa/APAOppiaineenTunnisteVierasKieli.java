package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;

public class APAOppiaineenTunnisteVierasKieli extends APAOppiaineenTunniste {

  public APAOppiaineenTunnisteVierasKieli(AikuistenPerusopetuksenAlkuvaiheenOppiaineet tunniste, Kielivalikoima kieli) {
    this.tunniste.setValue(tunniste);
    this.kieli.setValue(kieli);
  }
  
  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> getTunniste() {
    return tunniste;
  }
  
  public KoodistoViite<Kielivalikoima> getKieli() {
    return kieli;
  }

  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> tunniste = new KoodistoViite<>();
  private final KoodistoViite<Kielivalikoima> kieli = new KoodistoViite<>();
}
