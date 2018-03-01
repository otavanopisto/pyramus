package fi.otavanopisto.pyramus.koski;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KoskiStudentId {
  
  public KoskiStudentId() {
  }
  
  public KoskiStudentId(String studentIdentifier, String oid) {
    this.studentIdentifier = studentIdentifier;
    this.oid = oid;
  }
  
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }
  
  public String getStudentIdentifier() {
    return studentIdentifier;
  }

  public void setStudentIdentifier(String studentIdentifier) {
    this.studentIdentifier = studentIdentifier;
  }

  private String oid;
  private String studentIdentifier;
}
