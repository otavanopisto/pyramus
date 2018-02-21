package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssit;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionKurssinTunnisteValtakunnallinenOPS2015 extends LukionKurssinTunniste {

  public LukionKurssinTunnisteValtakunnallinenOPS2015() {
  }
  
  public LukionKurssinTunnisteValtakunnallinenOPS2015(LukionKurssit tunniste, 
      LukionKurssinTyyppi kurssinTyyppi) {
    super(kurssinTyyppi);
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<LukionKurssit> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<LukionKurssit> tunniste = new KoodistoViite<>();
}
