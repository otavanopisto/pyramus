package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.OppiaineMatematiikka;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class LukionOppiaineenSuoritusMatematiikka extends LukionOppiaineenTunniste {

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
