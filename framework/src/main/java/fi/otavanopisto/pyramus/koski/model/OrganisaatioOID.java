package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = JsonDeserializer.None.class)
public class OrganisaatioOID extends Organisaatio {

  public OrganisaatioOID() {
  }
  
  public OrganisaatioOID(String oid) {
    this.oid = oid;
  }
  
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  private String oid;
}
