package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOppiaineenTunnisteDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = LukionOppiaineenTunnisteDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOppiaineenTunniste {

  public LukionOppiaineenTunniste() {
  }
  
  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }

  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }

  private String perusteenDiaarinumero;
}
