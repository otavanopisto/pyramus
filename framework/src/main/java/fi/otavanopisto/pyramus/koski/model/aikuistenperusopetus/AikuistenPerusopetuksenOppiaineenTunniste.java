package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.deserializers.APOOppiaineenTunnisteDeserializer;

@JsonDeserialize(using = APOOppiaineenTunnisteDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenOppiaineenTunniste() {
  }
  
  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }

  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }

  private String perusteenDiaarinumero;
}
