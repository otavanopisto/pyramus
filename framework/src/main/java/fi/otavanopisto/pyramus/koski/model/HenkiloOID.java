package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HenkiloOID extends Henkilo {

  public HenkiloOID(String oid) {
    this.oid = oid;
  }
  
  @JsonProperty("oid")
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  private String oid;
}
