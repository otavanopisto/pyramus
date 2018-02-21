package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssitOPS2004Aikuiset;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class LukionKurssinTunnisteValtakunnallinenOPS2004 extends LukionKurssinTunniste {

  public LukionKurssinTunnisteValtakunnallinenOPS2004() {
  }
  
  public LukionKurssinTunnisteValtakunnallinenOPS2004(LukionKurssitOPS2004Aikuiset tunniste, 
      LukionKurssinTyyppi kurssinTyyppi) {
    super(kurssinTyyppi);
    this.tunniste.setValue(tunniste);
  }

  public KoodistoViite<LukionKurssitOPS2004Aikuiset> getTunniste() {
    return tunniste;
  }

  private final KoodistoViite<LukionKurssitOPS2004Aikuiset> tunniste = new KoodistoViite<>();
}
