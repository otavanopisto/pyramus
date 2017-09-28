package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class OpiskeluoikeusJakso {

  public OpiskeluoikeusJakso(Date alku, OpiskeluoikeudenTila tila) {
    this.alku = alku;
    this.tila.setValue(tila);
  }
  
  public Date getAlku() {
    return alku;
  }
  
  public KoodistoViite<OpiskeluoikeudenTila> getTila() {
    return tila;
  }
  
  private final Date alku;
  private final KoodistoViite<OpiskeluoikeudenTila> tila = new KoodistoViite<>();
}
