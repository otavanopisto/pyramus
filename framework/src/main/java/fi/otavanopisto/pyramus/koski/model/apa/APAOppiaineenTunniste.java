package fi.otavanopisto.pyramus.koski.model.apa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.APAOppiaineenTunnisteDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = APAOppiaineenTunnisteDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class APAOppiaineenTunniste {

}
