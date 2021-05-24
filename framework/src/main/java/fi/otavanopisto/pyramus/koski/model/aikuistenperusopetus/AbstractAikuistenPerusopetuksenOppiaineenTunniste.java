package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public abstract class AbstractAikuistenPerusopetuksenOppiaineenTunniste extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AbstractAikuistenPerusopetuksenOppiaineenTunniste() {
  }
  
  public AbstractAikuistenPerusopetuksenOppiaineenTunniste(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }
  
  public Laajuus getLaajuus() {
    return laajuus;
  }

  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  public boolean getPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  private boolean pakollinen;
  private Laajuus laajuus;
}
