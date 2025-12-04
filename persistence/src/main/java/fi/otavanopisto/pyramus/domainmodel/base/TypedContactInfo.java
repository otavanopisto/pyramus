package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class TypedContactInfo extends ContactInfo {

  @Override
  public boolean hasUniqueEmails() {
    return false;
  }

  public ContactType getContactType() {
    return contactType;
  }

  public void setContactType(ContactType contactType) {
    this.contactType = contactType;
  }

  @ManyToOne
  @JoinColumn (name = "contactType")
  private ContactType contactType;

}
