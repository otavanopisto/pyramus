package fi.otavanopisto.pyramus.ytl;

import java.util.ArrayList;
import java.util.List;

public class Kokelas {

  public String getHetu() {
    return hetu;
  }

  public void setHetu(String hetu) {
    this.hetu = hetu;
  }
  
  public String getOppijanumero() {
    return oppijanumero;
  }

  public void setOppijanumero(String oppijanumero) {
    this.oppijanumero = oppijanumero;
  }

  public List<String> getEtunimet() {
    return etunimet;
  }

  public void setEtunimet(List<String> etunimet) {
    this.etunimet = etunimet;
  }

  public String getSukunimi() {
    return sukunimi;
  }

  public void setSukunimi(String sukunimi) {
    this.sukunimi = sukunimi;
  }

  public Koulutustyyppi getKoulutustyyppi() {
    return koulutustyyppi;
  }

  public void setKoulutustyyppi(Koulutustyyppi koulutustyyppi) {
    this.koulutustyyppi = koulutustyyppi;
  }

  public Tutkintotyyppi getTutkintotyyppi() {
    return tutkintotyyppi;
  }

  public void setTutkintotyyppi(Tutkintotyyppi tutkintotyyppi) {
    this.tutkintotyyppi = tutkintotyyppi;
  }

  public boolean isUudelleenaloittaja() {
    return uudelleenaloittaja;
  }

  public void setUudelleenaloittaja(boolean uudelleenaloittaja) {
    this.uudelleenaloittaja = uudelleenaloittaja;
  }

  public int getKokelasnumero() {
    return kokelasnumero;
  }

  public void setKokelasnumero(int kokelasnumero) {
    this.kokelasnumero = kokelasnumero;
  }

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

  private String hetu;
  private String oppijanumero;
  private List<String> etunimet = new ArrayList<>();
  private String sukunimi;
  private Koulutustyyppi koulutustyyppi;
  private Tutkintotyyppi tutkintotyyppi;
  private boolean uudelleenaloittaja;
  private int kokelasnumero;
  private String äidinkielenKoe;
  private List<String> pakollisetKokeet = new ArrayList<>();
  private List<String> ylimääräisetKokeet = new ArrayList<>();
  private List<SuoritettuKurssi> suoritetutKurssit = new ArrayList<>();
}
