package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOppiaineenTunnisteDeserializer2019;

@JsonDeserialize(using = LukionOppiaineenTunnisteDeserializer2019.class)
public abstract class LukionOppiaineenTunniste2019 extends LukionOppiaineenKoulutusmoduuli {

  public LukionOppiaineenTunniste2019() {
  }
  
  public LukionOppiaineenTunniste2019(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }
  
  public boolean getPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  private boolean pakollinen;
}
