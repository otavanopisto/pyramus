package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.KoodistoViite;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOsasuoritusDeserializer2019;

@JsonDeserialize(using = LukionOsasuoritusDeserializer2019.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOsasuoritus2019 {

  public LukionOsasuoritus2019() {
  }
  
  public LukionOsasuoritus2019(SuorituksenTyyppi tyyppi) {
    this.tyyppi.setValue(tyyppi);
  }

  public void addOsasuoritus(LukionOpintojaksonSuoritus2019 lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public List<LukionOpintojaksonSuoritus2019> getOsasuoritukset() {
    return osasuoritukset;
  }

  public KoodistoViite<SuorituksenTyyppi> getTyyppi() {
    return tyyppi;
  }
  
  private final KoodistoViite<SuorituksenTyyppi> tyyppi = new KoodistoViite<>();
  private final List<LukionOpintojaksonSuoritus2019> osasuoritukset = new ArrayList<>();
}
