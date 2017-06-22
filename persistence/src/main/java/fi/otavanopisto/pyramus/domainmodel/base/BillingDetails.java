package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;

@Entity
public class BillingDetails {

  public Long getId() {
    return id;
  }

  public void setCompanyIdentifier(String companyIdentifier) {
    this.companyIdentifier = companyIdentifier;
  }

  public String getCompanyIdentifier() {
    return companyIdentifier;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setPersonName(String personName) {
    this.personName = personName;
  }

  public String getPersonName() {
    return personName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setStreetAddress1(String streetAddress1) {
    this.streetAddress1 = streetAddress1;
  }

  public String getStreetAddress1() {
    return streetAddress1;
  }

  public void setStreetAddress2(String streetAddress2) {
    this.streetAddress2 = streetAddress2;
  }

  public String getStreetAddress2() {
    return streetAddress2;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getRegion() {
    return region;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setElectronicBillingAddress(String electronicBillingAddress) {
    this.electronicBillingAddress = electronicBillingAddress;
  }
  
  public String getElectronicBillingAddress() {
    return electronicBillingAddress;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  @Transient
  public String toLine() {
    StringBuilder result = new StringBuilder();
    
    if (StringUtils.isNotBlank(getPersonName())) {
      result.append(getPersonName());
    }
    
    if (StringUtils.isNotBlank(getCompanyName())) {
      if (result.length() > 0) {
        result.append(" / ");
      }
      
      result.append(getCompanyName());
    }
    
    if (StringUtils.isNotBlank(getStreetAddress1())) {
      if (result.length() > 0) {
        result.append(", ");
      }
      
      result.append(getStreetAddress1());
    }
    
    if (StringUtils.isNotBlank(getPostalCode())) {
      if (result.length() > 0) {
        result.append(" ");
      }
      
      result.append(getPostalCode());
    }
    
    if (StringUtils.isNotBlank(getCity())) {
      if (result.length() > 0) {
        result.append(" ");
      }
      
      result.append(getCity());
    }
    
    if (StringUtils.isNotBlank(getCountry())) {
      if (result.length() > 0) {
        result.append(", ");
      }
      
      result.append(getCountry());
    }
    
    return result.toString();
  }

  public String getElectronicBillingOperator() {
    return electronicBillingOperator;
  }

  public void setElectronicBillingOperator(String electronicBillingOperator) {
    this.electronicBillingOperator = electronicBillingOperator;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="BillingDetails")  
  @TableGenerator(name="BillingDetails", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  private String personName;

  private String companyName;
  
  private String streetAddress1;

  private String streetAddress2;

  private String postalCode;

  private String city;

  private String region;
  
  private String country;

  private String phoneNumber;

  @Email
  private String emailAddress;
  
  private String companyIdentifier;

  private String referenceNumber;

  @Lob
  private String electronicBillingAddress;

  private String electronicBillingOperator;
  
  @Lob
  private String notes;
}
