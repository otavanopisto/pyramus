package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class Address {

  public Long getId() {
    return id;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }
  
  public String getCountry() {
    return country;
  }
  
  public void setCountry(String country) {
    this.country = country;
  }
  
  public String getCity() {
    return city;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setContactType(ContactType contactType) {
    this.contactType = contactType;
  }

  public ContactType getContactType() {
    return contactType;
  }

  public void setDefaultAddress(Boolean defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public Boolean getDefaultAddress() {
    return defaultAddress;
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
  
  @Transient
  public String toOneliner() {
    List<String> sequence = Arrays.asList(
        getName(),
        getStreetAddress(),
        getPostalCode(),
        getCity(),
        getCountry()
    );
    
    return sequence.stream().filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.joining(" "));
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Address")  
  @TableGenerator(name="Address", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @NotNull
  @Column(nullable = false)
  @Field
  private Boolean defaultAddress = Boolean.FALSE;

  @ManyToOne
  @JoinColumn (name = "contactType")
  private ContactType contactType;
  
  @Field (store = Store.NO)
  private String name;
  
  @Field (store = Store.NO)
  private String streetAddress;

  @Field (store = Store.NO)
  private String postalCode;

  @Field (store = Store.NO)
  private String city;
  
  @Field (store = Store.NO)
  private String country;

  @ManyToOne
  @JoinColumn(name="contactInfo", insertable=false, updatable=false)
  private ContactInfo contactInfo;

  @Version
  @Column(nullable = false)
  private Long version;
}
