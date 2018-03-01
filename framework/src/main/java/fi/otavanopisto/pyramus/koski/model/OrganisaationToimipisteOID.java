package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = JsonDeserializer.None.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisaationToimipisteOID extends OrganisaationToimipiste {

  public OrganisaationToimipisteOID() {
  }
  
  public OrganisaationToimipisteOID(String oid) {
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
