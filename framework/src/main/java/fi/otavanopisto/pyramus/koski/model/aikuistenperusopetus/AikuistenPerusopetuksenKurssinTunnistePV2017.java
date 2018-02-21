package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenPaattovaiheenKurssit2017;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class AikuistenPerusopetuksenKurssinTunnistePV2017 extends AikuistenPerusopetuksenKurssinTunniste {

  public AikuistenPerusopetuksenKurssinTunnistePV2017() {
  }
  
  public AikuistenPerusopetuksenKurssinTunnistePV2017(AikuistenPerusopetuksenPaattovaiheenKurssit2017 tunniste) {
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<AikuistenPerusopetuksenPaattovaiheenKurssit2017> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<AikuistenPerusopetuksenPaattovaiheenKurssit2017> tunniste = new KoodistoViite<>();
}
