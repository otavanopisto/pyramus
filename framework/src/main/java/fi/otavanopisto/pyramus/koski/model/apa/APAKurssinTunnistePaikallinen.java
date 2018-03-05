package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class APAKurssinTunnistePaikallinen extends APAKurssinTunniste {

  public APAKurssinTunnistePaikallinen() {
  }
  
  public APAKurssinTunnistePaikallinen(PaikallinenKoodi tunniste) {
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
