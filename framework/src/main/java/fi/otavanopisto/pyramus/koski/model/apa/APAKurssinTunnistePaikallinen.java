package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class APAKurssinTunnistePaikallinen extends APAKurssinTunniste {

  public APAKurssinTunnistePaikallinen() {
  }
  
  public APAKurssinTunnistePaikallinen(PaikallinenKoodi tunniste, Laajuus laajuus) {
    this.tunniste = tunniste;
    this.setLaajuus(laajuus);
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }

  private PaikallinenKoodi tunniste;
}
