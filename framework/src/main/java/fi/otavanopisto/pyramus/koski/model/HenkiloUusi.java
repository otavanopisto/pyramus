package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class HenkiloUusi extends Henkilo {

  public HenkiloUusi(String etunimet, String sukunimi, String kutsumanimi) {
    this.hetu = null;
    this.etunimet = etunimet;
    this.sukunimi = sukunimi;
    this.kutsumanimi = kutsumanimi;
  }
  
  public HenkiloUusi(String hetu, String etunimet, String sukunimi, String kutsumanimi) {
    this.hetu = hetu;
    this.etunimet = etunimet;
    this.sukunimi = sukunimi;
    this.kutsumanimi = kutsumanimi;
  }
  
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public String getHetu() {
    return hetu;
  }
  
  public String getEtunimet() {
    return etunimet;
  }
  
  public String getSukunimi() {
    return sukunimi;
  }
  
  public String getKutsumanimi() {
    return kutsumanimi;
  }
  
  private final String hetu;
  private final String etunimet;
  private final String sukunimi;
  private final String kutsumanimi;
}
