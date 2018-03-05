package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class APAOppiaineenTunnisteMuu extends APAOppiaineenTunniste {

  public APAOppiaineenTunnisteMuu() {
  }
  
  public APAOppiaineenTunnisteMuu(AikuistenPerusopetuksenAlkuvaiheenOppiaineet tunniste) {
    this.tunniste.setValue(tunniste);
  }
  
  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> tunniste = new KoodistoViite<>();
}
