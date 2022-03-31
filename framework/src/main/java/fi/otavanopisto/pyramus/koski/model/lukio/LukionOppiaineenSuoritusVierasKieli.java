package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenSuoritusVierasKieli extends AbstractLukionOppiaineenTunniste {

  public LukionOppiaineenSuoritusVierasKieli() {
  }
  
  public LukionOppiaineenSuoritusVierasKieli(KoskiOppiaineetYleissivistava tunniste, Kielivalikoima kieli, boolean pakollinen) {
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
