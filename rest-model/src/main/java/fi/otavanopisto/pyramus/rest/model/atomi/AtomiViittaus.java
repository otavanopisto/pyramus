package fi.otavanopisto.pyramus.rest.model.atomi;

public class AtomiViittaus {

  public String getTunniste() {
    return tunniste;
  }
  
  public void setTunniste(String tunniste) {
    this.tunniste = tunniste;
  }
  
  public String getNimi() {
    return nimi;
  }
  
  public void setNimi(String nimi) {
    this.nimi = nimi;
  }
  
  private String tunniste;
  private String nimi;
}
