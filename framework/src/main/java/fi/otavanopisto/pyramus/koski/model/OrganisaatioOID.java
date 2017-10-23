package fi.otavanopisto.pyramus.koski.model;

public class OrganisaatioOID extends Organisaatio {

  public OrganisaatioOID(String oid) {
    this.oid = oid;
  }
  
  public String getOid() {
    return oid;
  }

  private final String oid;
}
