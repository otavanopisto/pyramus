package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class HenkiloTiedotJaOID extends Henkilo {

  public HenkiloTiedotJaOID() {
  }
  
  public HenkiloTiedotJaOID(String oid, String etunimet, String sukunimi, String kutsumanimi) {
    this.oid = oid;
    this.hetu = null;
    this.etunimet = etunimet;
    this.sukunimi = sukunimi;
    this.kutsumanimi = kutsumanimi;
  }
  
  public HenkiloTiedotJaOID(String oid, String hetu, String etunimet, String sukunimi, String kutsumanimi) {
    this.oid = oid;
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
  
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public void setHetu(String hetu) {
    this.hetu = hetu;
  }

  public void setEtunimet(String etunimet) {
    this.etunimet = etunimet;
  }

  public void setSukunimi(String sukunimi) {
    this.sukunimi = sukunimi;
  }

  public void setKutsumanimi(String kutsumanimi) {
    this.kutsumanimi = kutsumanimi;
  }

  private String oid;
  private String hetu;
  private String etunimet;
  private String sukunimi;
  private String kutsumanimi;
}
