package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class HenkiloTiedotJaOID extends Henkilo {

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

  private final String oid;
  private final String hetu;
  private final String etunimet;
  private final String sukunimi;
  private final String kutsumanimi;
}
