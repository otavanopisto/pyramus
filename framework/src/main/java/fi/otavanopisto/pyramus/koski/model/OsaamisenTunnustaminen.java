package fi.otavanopisto.pyramus.koski.model;

public class OsaamisenTunnustaminen {

  public OsaamisenTunnustaminen(Object osaaminen) {
    this.osaaminen = osaaminen;
  }
  
  public Kuvaus getSelite() {
    return selite;
  }

  public Object getOsaaminen() {
    return osaaminen;
  }

  private final Object osaaminen;
  private final Kuvaus selite = new Kuvaus();
}
