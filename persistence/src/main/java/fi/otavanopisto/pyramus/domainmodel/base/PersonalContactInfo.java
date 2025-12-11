package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Entity;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class PersonalContactInfo extends ContactInfo {

  @Override
  public boolean hasUniqueEmails() {
    return true;
  }

}
