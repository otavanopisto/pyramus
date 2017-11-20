package fi.otavanopisto.pyramus.koski.model.apa;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

public abstract class APAKurssinTunniste {

  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }
  
  private Laajuus laajuus;
}
