package fi.otavanopisto.pyramus.rest.model.atomi;

import java.time.LocalDate;

public class Opiskelija {

  public String getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(String tunniste) {
    this.tunniste = tunniste;
  }
  
  public String getEtunimet() {
    return etunimet;
  }
  
  public void setEtunimet(String etunimet) {
    this.etunimet = etunimet;
  }
  
  public String getSukunimi() {
    return sukunimi;
  }
  
  public void setSukunimi(String sukunimi) {
    this.sukunimi = sukunimi;
  }
  
  public LocalDate getSyntymäaika() {
    return syntymäaika;
  }
  
  public void setSyntymäaika(LocalDate syntymäaika) {
    this.syntymäaika = syntymäaika;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getKäyttäjätunnus() {
    return käyttäjätunnus;
  }

  public void setKäyttäjätunnus(String käyttäjätunnus) {
    this.käyttäjätunnus = käyttäjätunnus;
  }

  public String getOpiskelijanumero() {
    return opiskelijanumero;
  }

  public void setOpiskelijanumero(String opiskelijanumero) {
    this.opiskelijanumero = opiskelijanumero;
  }

  private String tunniste;
  private String käyttäjätunnus;
  private String opiskelijanumero;
  private String etunimet;
  private String sukunimi;
  private LocalDate syntymäaika;
  private String email;
}
