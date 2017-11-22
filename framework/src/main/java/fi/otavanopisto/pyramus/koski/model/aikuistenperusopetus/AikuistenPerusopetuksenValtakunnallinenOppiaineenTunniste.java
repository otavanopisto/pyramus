package fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus;

public class AikuistenPerusopetuksenValtakunnallinenOppiaineenTunniste extends AikuistenPerusopetuksenOppiaineenTunniste {

  public AikuistenPerusopetuksenValtakunnallinenOppiaineenTunniste(boolean pakollinen) {
    super();
    this.pakollinen = pakollinen;
  }

  public boolean getPakollinen() {
    return pakollinen;
  }

  public void setPakollinen(boolean pakollinen) {
    this.pakollinen = pakollinen;
  }

  private boolean pakollinen;
}
