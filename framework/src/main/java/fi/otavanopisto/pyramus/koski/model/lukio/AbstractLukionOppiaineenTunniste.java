package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public abstract class AbstractLukionOppiaineenTunniste extends LukionOppiaineenTunniste {

  public AbstractLukionOppiaineenTunniste() {
  }
  
  public AbstractLukionOppiaineenTunniste(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }
  
  public boolean getPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  public Laajuus getLaajuus() {
    return laajuus;
  }

  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  private boolean pakollinen;
  private Laajuus laajuus;
}
