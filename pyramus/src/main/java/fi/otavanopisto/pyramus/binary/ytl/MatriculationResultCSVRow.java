package fi.otavanopisto.pyramus.binary.ytl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * https://github.com/digabi/ilmoittautuminen/wiki/Lopullisen-arvostelun-tulokset
 */
// Jackson fails to parse koulu_nro field and nothing seems to help with that
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatriculationResultCSVRow {

  public MatriculationResultCSVRow() {
  }

  public String getKoulu_nro() {
    return koulu_nro;
  }

  public void setKoulu_nro(String koulu_nro) {
    this.koulu_nro = koulu_nro;
  }

  public String getKokelaslaji() {
    return kokelaslaji;
  }

  public void setKokelaslaji(String kokelaslaji) {
    this.kokelaslaji = kokelaslaji;
  }

  public String getKokelasnumero() {
    return kokelasnumero;
  }

  public void setKokelasnumero(String kokelasnumero) {
    this.kokelasnumero = kokelasnumero;
  }

  public String getHetu() {
    return hetu;
  }

  public void setHetu(String hetu) {
    this.hetu = hetu;
  }

  public String getKoe() {
    return koe;
  }

  public void setKoe(String koe) {
    this.koe = koe;
  }

  public String getTutkintokerta() {
    return tutkintokerta;
  }

  public void setTutkintokerta(String tutkintokerta) {
    this.tutkintokerta = tutkintokerta;
  }

  public String getTehtavapisteet() {
    return tehtavapisteet;
  }

  public void setTehtavapisteet(String tehtavapisteet) {
    this.tehtavapisteet = tehtavapisteet;
  }

  public String getYhteispisteet() {
    return yhteispisteet;
  }

  public void setYhteispisteet(String yhteispisteet) {
    this.yhteispisteet = yhteispisteet;
  }

  public String getArvosana() {
    return arvosana;
  }

  public void setArvosana(String arvosana) {
    this.arvosana = arvosana;
  }

  private String koulu_nro;
  private String kokelaslaji;
  private String kokelasnumero;
  private String hetu;
  private String koe;
  private String tutkintokerta;
  private String tehtavapisteet;
  private String yhteispisteet;
  private String arvosana;
}