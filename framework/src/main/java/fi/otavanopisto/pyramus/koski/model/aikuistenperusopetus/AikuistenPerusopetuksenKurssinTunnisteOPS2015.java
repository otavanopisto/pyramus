package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenKurssit2015;
import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenKurssinTunnisteOPS2015 extends AikuistenPerusopetuksenKurssinTunniste {

  public AikuistenPerusopetuksenKurssinTunnisteOPS2015() {
  }
  
  public AikuistenPerusopetuksenKurssinTunnisteOPS2015(AikuistenPerusopetuksenKurssit2015 tunniste, Laajuus laajuus) {
    this.tunniste.setValue(tunniste);
    this.setLaajuus(laajuus);
  }

  public KoodistoViite<AikuistenPerusopetuksenKurssit2015> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<AikuistenPerusopetuksenKurssit2015> tunniste = new KoodistoViite<>();
}
