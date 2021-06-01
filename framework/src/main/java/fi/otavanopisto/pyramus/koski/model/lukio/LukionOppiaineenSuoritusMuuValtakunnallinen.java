package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenSuoritusMuuValtakunnallinen extends AbstractLukionOppiaineenTunniste {

  public LukionOppiaineenSuoritusMuuValtakunnallinen() {
  }
  
  public LukionOppiaineenSuoritusMuuValtakunnallinen(KoskiOppiaineetYleissivistava tunniste, boolean pakollinen) {
    super(pakollinen);
    this.tunniste.setValue(tunniste);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>();
}
