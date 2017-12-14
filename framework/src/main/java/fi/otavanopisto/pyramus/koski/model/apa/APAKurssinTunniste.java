package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.otavanopisto.pyramus.koski.model.Laajuus;

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
