package fi.otavanopisto.pyramus.koski.model;

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
