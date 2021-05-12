package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.LukionOppimaara;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;

public class LukionSuoritus2019 extends LukionSuoritus {

  public LukionSuoritus2019() {
  }
  
  public LukionSuoritus2019(SuorituksenTyyppi tyyppi, LukionOppimaara oppimaara, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(tyyppi, suorituskieli, toimipiste);
    this.oppimaara.setValue(oppimaara);
  }
  
  @JsonProperty("oppimäärä")
  public KoodistoViite<LukionOppimaara> getOppimaara() {
    return oppimaara;
  }
  
  private final KoodistoViite<LukionOppimaara> oppimaara = new KoodistoViite<>();
}
