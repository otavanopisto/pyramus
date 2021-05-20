package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.deserializers.LukionKurssinTunnisteDeserializer2019;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = LukionKurssinTunnisteDeserializer2019.class)
public abstract class LukionOpintojaksonTunniste2019 {

  public LukionOpintojaksonTunniste2019() {
  }
  
  public LukionOpintojaksonTunniste2019(Laajuus laajuus, boolean pakollinen) {
    this.laajuus = laajuus;
    this.pakollinen = pakollinen;
  }
  
  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  public boolean isPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  private Laajuus laajuus;
  private boolean pakollinen;
}
