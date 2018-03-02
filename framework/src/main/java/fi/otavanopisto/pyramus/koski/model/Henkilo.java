package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.HenkiloDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = HenkiloDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Henkilo {

}
