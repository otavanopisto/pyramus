package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.APOOsasuoritusDeserializer;

@JsonDeserialize(using = APOOsasuoritusDeserializer.class)
public abstract class AikuistenPerusopetuksenOsasuoritus {

}
