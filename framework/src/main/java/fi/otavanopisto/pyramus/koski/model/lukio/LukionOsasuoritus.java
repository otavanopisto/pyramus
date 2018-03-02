package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOsasuoritusDeserializer;

@JsonDeserialize(using = LukionOsasuoritusDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOsasuoritus {

}
