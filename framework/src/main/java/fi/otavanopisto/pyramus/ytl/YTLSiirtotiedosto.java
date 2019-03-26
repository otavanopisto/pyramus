package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.List;

public class YTLSiirtotiedosto {

  public YTLSiirtotiedosto() {
  }
  
  public YTLSiirtotiedosto(String tutkintokerta, Integer koulunumero) {
    this.tutkintokerta = tutkintokerta;
    this.koulunumero = koulunumero;
  }
  
  public String getTutkintokerta() {
    return tutkintokerta;
  }

  public void setTutkintokerta(String tutkintokerta) {
    this.tutkintokerta = tutkintokerta;
  }
  
  public Integer getKoulunumero() {
    return koulunumero;
  }

  public void setKoulunumero(Integer koulunumero) {
    this.koulunumero = koulunumero;
  }

  public void addKokelas(Kokelas kokelas) {
    this.kokelaat.add(kokelas);
  }
  
  public List<Kokelas> getKokelaat() {
    return kokelaat;
  }

  public void setKokelaat(List<Kokelas> kokelaat) {
    this.kokelaat = kokelaat;
  }

  private String tutkintokerta;
  private Integer koulunumero;
  private List<Kokelas> kokelaat = new ArrayList<>();
}