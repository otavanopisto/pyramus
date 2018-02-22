package fi.otavanopisto.pyramus.koski.model;

public class OrganisaatioHenkilo {

  public OrganisaatioHenkilo() {
  }
  
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

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public void setTitteli(Kuvaus titteli) {
    this.titteli = titteli;
  }

  public void setOrganisaatio(Organisaatio organisaatio) {
    this.organisaatio = organisaatio;
  }

  private String nimi;
  private Kuvaus titteli;
  private Organisaatio organisaatio;
}
