package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusEiTiedossa() {
    super();
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>(KoskiOppiaineetYleissivistava.XX);
}
