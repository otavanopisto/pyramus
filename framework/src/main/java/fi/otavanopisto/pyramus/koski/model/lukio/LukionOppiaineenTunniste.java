package fi.otavanopisto.pyramus.koski.model.lukio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.otavanopisto.pyramus.koski.model.Laajuus;
import fi.otavanopisto.pyramus.koski.model.deserializers.LukionOppiaineenTunnisteDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = LukionOppiaineenTunnisteDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LukionOppiaineenTunniste {

  public LukionOppiaineenTunniste() {
  }
  
  public LukionOppiaineenTunniste(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }
  
  public boolean getPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }

  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }

  public Laajuus getLaajuus() {
    return laajuus;
  }

  public void setLaajuus(Laajuus laajuus) {
    this.laajuus = laajuus;
  }

  private boolean pakollinen;
  private String perusteenDiaarinumero;
  private Laajuus laajuus;
}
