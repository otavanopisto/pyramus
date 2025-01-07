package fi.otavanopisto.pyramus.koski;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Maps multiple Koski OIDs for one Pyramus Student in
 * cases where the Student's credits are split into
 * multiple study permissions.
 * 
 * This is legacy code that should be removed. A single
 * Student should always have only one study permission.
 * 
 * Note that Pyramus Persistence has a separate instance
 * of this as it cannot reference this class but needs
 * to be able to parse the same structure in order to 
 * provide indexing of Person's OIDs. Thus any changes
 * to this class should be reflected to Persistence's 
 * PersonKoskiStudentOIDFieldIndexer.
 */
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
