package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
