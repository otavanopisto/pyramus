package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineAidinkieliJaKirjallisuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli extends AbstractAikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli() {
  }
  
  public AikuistenPerusopetuksenOppiaineenSuoritusAidinkieli(OppiaineAidinkieliJaKirjallisuus kieli, boolean pakollinen) {
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
