package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOsasuoritusDeserializer;

@JsonDeserialize(using = LukionOsasuoritusDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOsasuoritus2019 {

  public void addOsasuoritus(LukionOpintojaksonSuoritus2019 lukionKurssinSuoritus) {
    osasuoritukset.add(lukionKurssinSuoritus);
  }
  
  public List<LukionOpintojaksonSuoritus2019> getOsasuoritukset() {
    return osasuoritukset;
  }

  private final List<LukionOpintojaksonSuoritus2019> osasuoritukset = new ArrayList<>();
}
