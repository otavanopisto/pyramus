package fi.otavanopisto.pyramus.rest.model.atomi;

public class KirjautunutKayttaja {

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

  public Rooli getRooli() {
    return rooli;
  }

  public void setRooli(Rooli rooli) {
    this.rooli = rooli;
  }

  public String getOpiskelijatunniste() {
    return opiskelijatunniste;
  }

  public void setOpiskelijatunniste(String opiskelijatunniste) {
    this.opiskelijatunniste = opiskelijatunniste;
  }

  private String opiskelijatunniste;
  private String etunimet;
  private String sukunimi;
  private Rooli rooli;
}
