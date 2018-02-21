package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenKurssinTunnistePaikallinen extends AikuistenPerusopetuksenKurssinTunniste {

  public AikuistenPerusopetuksenKurssinTunnistePaikallinen() {
  }
  
  public AikuistenPerusopetuksenKurssinTunnistePaikallinen(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }

  private PaikallinenKoodi tunniste;
}
