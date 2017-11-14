package fi.otavanopisto.pyramus.koski.model.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

  @JsonProperty("lähdejärjestelmänId")
  public LahdejarjestelmaIdReturnVal getLahdejarjestelmanId() {
    return lahdejarjestelmanId;
  }

  public void setLahdejarjestelmanId(LahdejarjestelmaIdReturnVal lahdejarjestelmanId) {
    this.lahdejarjestelmanId = lahdejarjestelmanId;
  }

  private String oid;
  private Long versionumero;
  private LahdejarjestelmaIdReturnVal lahdejarjestelmanId;
}
