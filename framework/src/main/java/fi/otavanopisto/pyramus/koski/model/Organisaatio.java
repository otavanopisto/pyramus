package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.OrganisaatioDeserializer;

@JsonDeserialize(using = OrganisaatioDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Organisaatio {

}
