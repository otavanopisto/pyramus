package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.AikuistenPerusopetuksenAlkuvaiheenKurssit2017;
import fi.otavanopisto.pyramus.koski.model.Laajuus;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class APAKurssinTunnisteOPS2017 extends APAKurssinTunniste {

  public APAKurssinTunnisteOPS2017() {
  }
  
  public APAKurssinTunnisteOPS2017(AikuistenPerusopetuksenAlkuvaiheenKurssit2017 tunniste, Laajuus laajuus) {
    this.tunniste.setValue(tunniste);
    this.setLaajuus(laajuus);
  }

  public KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenKurssit2017> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<AikuistenPerusopetuksenAlkuvaiheenKurssit2017> tunniste = new KoodistoViite<>();
}
