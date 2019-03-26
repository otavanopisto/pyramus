package fi.otavanopisto.pyramus.ytl;

public class SuoritettuKurssi {

  public SuoritettuKurssi() {
  }

  public SuoritettuKurssi(String aine, String oppimäärä, Integer kursseja) {
    this.aine = aine;
    this.setOppimäärä(oppimäärä);
    this.kursseja = kursseja;
  }
  
  public String getAine() {
    return aine;
  }
  
  public void setAine(String aine) {
    this.aine = aine;
  }

  public String getOppimäärä() {
    return oppimäärä;
  }

  public void setOppimäärä(String oppimäärä) {
    this.oppimäärä = oppimäärä;
  }

  public Integer getKursseja() {
    return kursseja;
  }

  public void setKursseja(Integer kursseja) {
    this.kursseja = kursseja;
  }

  private String aine;
  private String oppimäärä;
  private Integer kursseja;
}
