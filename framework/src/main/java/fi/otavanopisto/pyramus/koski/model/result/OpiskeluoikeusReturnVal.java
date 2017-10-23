package fi.otavanopisto.pyramus.koski.model.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpiskeluoikeusReturnVal {

  public String getOid() {
    return oid;
  }
  
  public void setOid(String oid) {
    this.oid = oid;
  }
  
  public Long getVersionumero() {
    return versionumero;
  }
  
  public void setVersionumero(Long versionumero) {
    this.versionumero = versionumero;
  }

  private String oid;
  private Long versionumero;
}
