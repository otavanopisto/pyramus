package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOppiaineenTunnisteDeserializer2019;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = LukionOppiaineenTunnisteDeserializer2019.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOppiaineenKoulutusmoduuli {

  public Laajuus getLaajuus() {
    return laajuus;
  }

  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  private Laajuus laajuus;
}
