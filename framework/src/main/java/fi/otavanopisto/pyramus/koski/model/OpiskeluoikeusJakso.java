package fi.otavanopisto.pyramus.koski.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenRahoitus;
import fi.otavanopisto.pyramus.koski.koodisto.OpiskeluoikeudenTila;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
  
  public KoodistoViite<OpintojenRahoitus> getOpintojenRahoitus() {
    return opintojenRahoitus;
  }

  public void setOpintojenRahoitus(KoodistoViite<OpintojenRahoitus> opintojenRahoitus) {
    this.opintojenRahoitus = opintojenRahoitus;
  }

  private final Date alku;
  private final KoodistoViite<OpiskeluoikeudenTila> tila = new KoodistoViite<>();
  private KoodistoViite<OpintojenRahoitus> opintojenRahoitus;
}
