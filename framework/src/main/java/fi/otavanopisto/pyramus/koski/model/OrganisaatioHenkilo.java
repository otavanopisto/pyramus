package fi.otavanopisto.pyramus.koski.model;

public class OrganisaatioHenkilo {

  public OrganisaatioHenkilo(String nimi, Kuvaus titteli, Organisaatio organisaatio) {
    this.nimi = nimi;
    this.titteli = titteli;
    this.organisaatio = organisaatio;
  }
  
  public String getNimi() {
    return nimi;
  }
  
  public Kuvaus getTitteli() {
    return titteli;
  }
  
  public Organisaatio getOrganisaatio() {
    return organisaatio;
  }

  private final String nimi;
  private final Kuvaus titteli;
  private final Organisaatio organisaatio;
}
