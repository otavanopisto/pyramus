package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
public class PhoneNumber {

  public Long getId() {
    return id;
  }

  public void setContactType(ContactType contactType) {
    this.contactType = contactType;
  }

  public ContactType getContactType() {
    return contactType;
  }

  public void setDefaultNumber(Boolean defaultNumber) {
    this.defaultNumber = defaultNumber;
  }

  public Boolean getDefaultNumber() {
    return defaultNumber;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }

  public ContactInfo getContactInfo() {
    return contactInfo;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="PhoneNumber")  
  @TableGenerator(name="PhoneNumber", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "contactType")
  private ContactType contactType;

  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean defaultNumber = Boolean.FALSE;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  @Field (store = Store.NO) 
  private String number;

  @ManyToOne
  @JoinColumn(name="contactInfo", insertable=false, updatable=false)
  private ContactInfo contactInfo;
 
  @Version
  @Column(nullable = false)
  private Long version;
}