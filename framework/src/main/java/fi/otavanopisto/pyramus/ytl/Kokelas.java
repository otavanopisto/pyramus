package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.List;

/**
 * Kokelas-tietue ennen vuoden 2022 muutoksia.
 */
public class Kokelas extends AbstractKokelas {

  public String getÄidinkielenKoe() {
    return äidinkielenKoe;
  }

  public void setÄidinkielenKoe(String äidinkielenKoe) {
    this.äidinkielenKoe = äidinkielenKoe;
  }

  public List<String> getPakollisetKokeet() {
    return pakollisetKokeet;
  }

  public void addPakollinenKoe(String pakollinenKoe) {
    this.pakollisetKokeet.add(pakollinenKoe);
  }
  
  public void setPakollisetKokeet(List<String> pakollisetKokeet) {
    this.pakollisetKokeet = pakollisetKokeet;
  }

  public void addYlimääräinenKoe(String ylimääräinenKoe) {
    this.ylimääräisetKokeet.add(ylimääräinenKoe);
  }
  
  public List<String> getYlimääräisetKokeet() {
    return ylimääräisetKokeet;
  }

  public void setYlimääräisetKokeet(List<String> ylimääräisetKokeet) {
    this.ylimääräisetKokeet = ylimääräisetKokeet;
  }

  public List<SuoritettuKurssi> getSuoritetutKurssit() {
    return suoritetutKurssit;
  }

  public void setSuoritetutKurssit(List<SuoritettuKurssi> suoritetutKurssit) {
    this.suoritetutKurssit = suoritetutKurssit;
  }

  private String äidinkielenKoe;
  private List<String> pakollisetKokeet = new ArrayList<>();
  private List<String> ylimääräisetKokeet = new ArrayList<>();
  private List<SuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
}
