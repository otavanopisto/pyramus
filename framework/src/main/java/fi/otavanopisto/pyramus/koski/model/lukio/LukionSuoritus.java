package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTila;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.Suoritus;

public abstract class LukionSuoritus extends Suoritus {

  public LukionSuoritus(SuorituksenTila tila, SuorituksenTyyppi tyyppi, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(tila, tyyppi, suorituskieli, toimipiste);
  }

}
