package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsaamisenTunnustaminen {

  public OsaamisenTunnustaminen() {
  }
  
  public OsaamisenTunnustaminen(Kuvaus selite) {
    this.selite = selite;
  }
  
  public Kuvaus getSelite() {
    return selite;
  }

  public void setSelite(Kuvaus selite) {
    this.selite = selite;
  }

  public Object getOsaaminen() {
    return osaaminen;
  }

  public void setOsaaminen(Object osaaminen) {
    this.osaaminen = osaaminen;
  }

  public boolean isRahoituksenPiirissä() {
    return rahoituksenPiirissä;
  }

  public void setRahoituksenPiirissä(boolean rahoituksenPiirissä) {
    this.rahoituksenPiirissä = rahoituksenPiirissä;
  }

  private Object osaaminen;
  private Kuvaus selite;
  private boolean rahoituksenPiirissä = false;
}
