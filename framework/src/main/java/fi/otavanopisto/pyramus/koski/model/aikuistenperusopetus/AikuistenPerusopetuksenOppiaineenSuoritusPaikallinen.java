package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Kuvaus;
import fi.otavanopisto.pyramus.koski.model.PaikallinenKoodi;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen extends AbstractAikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen() {
  }
  
  public AikuistenPerusopetuksenOppiaineenSuoritusPaikallinen(PaikallinenKoodi tunniste, boolean pakollinen, Kuvaus kuvaus) {
    super(pakollinen);
    this.tunniste = tunniste;
    this.kuvaus = kuvaus;
  }
  
  public PaikallinenKoodi getTunniste() {
    return tunniste;
  }
  
  public Kuvaus getKuvaus() {
    return kuvaus;
  }

  public void setTunniste(PaikallinenKoodi tunniste) {
    this.tunniste = tunniste;
  }

  public void setKuvaus(Kuvaus kuvaus) {
    this.kuvaus = kuvaus;
  }

  private PaikallinenKoodi tunniste;
  private Kuvaus kuvaus;
}
