package fi.otavanopisto.pyramus.koski;

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
