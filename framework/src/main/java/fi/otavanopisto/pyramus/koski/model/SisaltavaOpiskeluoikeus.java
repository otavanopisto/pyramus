package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SisaltavaOpiskeluoikeus {

  public SisaltavaOpiskeluoikeus(Oppilaitos oppilaitos, String oid) {
    this.oppilaitos = oppilaitos;
    this.oid = oid;
  }
  
  public Oppilaitos getOppilaitos() {
    return oppilaitos;
  }
  
  public void setOppilaitos(Oppilaitos oppilaitos) {
    this.oppilaitos = oppilaitos;
  }

  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  private Oppilaitos oppilaitos;
  private String oid;
}
