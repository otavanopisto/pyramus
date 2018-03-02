package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionOppiaineenSuoritusMatematiikka extends LukionOppiaineenTunniste {

  public LukionOppiaineenSuoritusMatematiikka() {
  }
  
  public LukionOppiaineenSuoritusMatematiikka(OppiaineMatematiikka oppimaara, boolean pakollinen) {
    super(pakollinen);
    this.oppimaara.setValue(oppimaara);
  }
  
  public KoodistoViite<KoskiOppiaineetYleissivistava> getTunniste() {
    return tunniste;
  }
  
  @JsonProperty("oppimäärä")
  public KoodistoViite<OppiaineMatematiikka> getOppimaara() {
    return oppimaara;
  }

  private final KoodistoViite<KoskiOppiaineetYleissivistava> tunniste = new KoodistoViite<>(KoskiOppiaineetYleissivistava.MA);
  private final KoodistoViite<OppiaineMatematiikka> oppimaara = new KoodistoViite<>();
}
