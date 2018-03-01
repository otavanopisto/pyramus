package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.OrganisaationToimipisteDeserializer;

@JsonDeserialize(using = OrganisaationToimipisteDeserializer.class)
public abstract class OrganisaationToimipiste {
  
}
