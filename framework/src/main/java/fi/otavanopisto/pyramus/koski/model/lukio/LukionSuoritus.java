package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.koodisto.Kieli;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.OrganisaationToimipiste;
import fi.otavanopisto.pyramus.koski.model.Suoritus;
import fi.otavanopisto.pyramus.koski.model.deserializers.SuoritusDeserializer;

@JsonDeserialize(using = SuoritusDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionSuoritus extends Suoritus {

  public LukionSuoritus() {
  }
  
  public LukionSuoritus(SuorituksenTyyppi tyyppi, Kieli suorituskieli, OrganisaationToimipiste toimipiste) {
    super(tyyppi, suorituskieli, toimipiste);
  }

}
