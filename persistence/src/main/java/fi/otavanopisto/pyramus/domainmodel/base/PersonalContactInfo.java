package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Entity;

@Entity
public class PersonalContactInfo extends ContactInfo {

  @Override
  public boolean hasUniqueEmails() {
    return true;
  }

}
