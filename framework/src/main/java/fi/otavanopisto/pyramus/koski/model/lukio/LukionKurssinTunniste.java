package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.LukionKurssinTyyppi;
import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.deserializers.LukionKurssinTunnisteDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = LukionKurssinTunnisteDeserializer.class)
public abstract class LukionKurssinTunniste {

  public LukionKurssinTunniste() {
  }
  
  public LukionKurssinTunniste(LukionKurssinTyyppi kurssinTyyppi) {
    this.kurssinTyyppi.setValue(kurssinTyyppi);
  }
  
  public Laajuus getLaajuus() {
    return laajuus;
  }
  
  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  public KoodistoViite<LukionKurssinTyyppi> getKurssinTyyppi() {
    return kurssinTyyppi;
  }
  
  private Laajuus laajuus;
  private final KoodistoViite<LukionKurssinTyyppi> kurssinTyyppi = new KoodistoViite<>();
}
