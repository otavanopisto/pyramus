package fi.otavanopisto.pyramus.koski.model;

public class OrganisaationToimipisteOID extends OrganisaationToimipiste {

  public OrganisaationToimipisteOID(String oid) {
    this.oid = oid;
  }
  
  public String getOid() {
    return oid;
  }

  private final String oid;
}
