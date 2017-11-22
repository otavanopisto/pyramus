package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AikuistenPerusopetuksenKurssinTunniste {

  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }
  
  private Laajuus laajuus;
}
