package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

public abstract class AikuistenPerusopetuksenKurssinTunniste {

  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }
  
  private Laajuus laajuus;
}
