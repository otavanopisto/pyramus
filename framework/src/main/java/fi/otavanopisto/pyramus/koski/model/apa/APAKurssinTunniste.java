package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.deserializers.APAKurssinTunnisteDeserializer;

@JsonDeserialize(using = APAKurssinTunnisteDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class APAKurssinTunniste {

  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }
  
  private Laajuus laajuus;
}
