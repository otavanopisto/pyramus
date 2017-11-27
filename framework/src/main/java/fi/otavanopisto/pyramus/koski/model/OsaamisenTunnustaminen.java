package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OsaamisenTunnustaminen {

  public OsaamisenTunnustaminen(Kuvaus selite) {
    this.selite = selite;
  }
  
  public Kuvaus getSelite() {
    return selite;
  }

  public Object getOsaaminen() {
    return osaaminen;
  }

  public void setOsaaminen(Object osaaminen) {
    this.osaaminen = osaaminen;
  }

  private Object osaaminen;
  private final Kuvaus selite;
}
