package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

public abstract class LukionOppiaineenTunniste2019 extends LukionOppiaineenKoulutusmoduuli {

  public LukionOppiaineenTunniste2019() {
  }
  
  public LukionOppiaineenTunniste2019(boolean pakollinen) {
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
