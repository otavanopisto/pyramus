package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenOppiaineet;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class APAOppiaineenTunnisteAidinkieli extends APAOppiaineenTunniste {

  public APAOppiaineenTunnisteAidinkieli() {
  }
  
  public APAOppiaineenTunnisteAidinkieli(OppiaineAidinkieliJaKirjallisuus kieli) {
    this.kieli.setValue(kieli);
  }
  
  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> getTunniste() {
    return tunniste;
  }
  
  public KoodistoViite<OppiaineAidinkieliJaKirjallisuus> getKieli() {
    return kieli;
  }

  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenOppiaineet> tunniste = new KoodistoViite<>(AikuistenPerusopetuksenAlkuvaiheenOppiaineet.AI);
  private final KoodistoViite<OppiaineAidinkieliJaKirjallisuus> kieli = new KoodistoViite<>();
}
