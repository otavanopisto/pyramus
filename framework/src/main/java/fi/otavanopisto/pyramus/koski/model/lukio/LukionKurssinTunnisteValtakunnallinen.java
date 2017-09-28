package fi.otavanopisto.pyramus.koski.model.lukio;

import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;
import fi.otavanopisto.pyramus.koski.koodisto.test.KoodistoViite;

public class LukionKurssinTunnisteValtakunnallinen extends LukionKurssinTunniste {

  public LukionKurssinTunnisteValtakunnallinen(LukionKurssit tunniste, 
      LukionKurssinTyyppi kurssinTyyppi) {
    super(kurssinTyyppi);
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<LukionKurssit> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<LukionKurssit> tunniste = new KoodistoViite<>();
}
