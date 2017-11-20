package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;

public class APAOppiaineenTunnisteMuu extends APAOppiaineenTunniste {

  public APAOppiaineenTunnisteMuu(AikuistenPerusopetuksenAlkuvaiheenOppiaineet tunniste) {
    this.tunniste.setValue(tunniste);
  }
  
  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> tunniste = new KoodistoViite<>();
}
