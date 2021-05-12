package fi.otavanopisto.pyramus.koski.model.lukio.ops2019;

public class LukionOppiaineenOppimaaranKoulutusmoduuli2019 {

  public String getPerusteenDiaarinumero() {
    return perusteenDiaarinumero;
  }

  public void setPerusteenDiaarinumero(String perusteenDiaarinumero) {
    this.perusteenDiaarinumero = perusteenDiaarinumero;
  }

  public LukionOppiaineidenOppimaaratKoodi2019 getTunniste() {
    return tunniste;
  }

  private final LukionOppiaineidenOppimaaratKoodi2019 tunniste = new LukionOppiaineidenOppimaaratKoodi2019();
  private String perusteenDiaarinumero;
  
  public class LukionOppiaineidenOppimaaratKoodi2019 {
    public final String koodiarvo = "lukionaineopinnot";
  }
}
