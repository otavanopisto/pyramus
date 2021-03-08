package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

public class Organization {

  public Organization() {
    super();
  }

  public Organization(Long id, String name, Boolean archived) {
    this(id, name, null, archived);
  }

  public Organization(Long id, String name, BillingDetails billingDetails, Boolean archived) {
    super();
    this.id = id;
    this.name = name;
    this.billingDetails = billingDetails;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public BillingDetails getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(BillingDetails billingDetails) {
    this.billingDetails = billingDetails;
  }

  public List<OrganizationContactPerson> getContactPersons() {
    return contactPersons;
  }

  public void setContactPersons(List<OrganizationContactPerson> contactPersons) {
    this.contactPersons = contactPersons;
  }

  public EducationType getEducationType() {
    return educationType;
  }

  public void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  public List<OrganizationContractPeriod> getContractPeriods() {
    return contractPeriods;
  }

  public void setContractPeriods(List<OrganizationContractPeriod> contractPeriods) {
    this.contractPeriods = contractPeriods;
  }

  private Long id;
  private String name;
  private EducationType educationType;
  private List<OrganizationContractPeriod> contractPeriods;
  private List<OrganizationContactPerson> contactPersons;
  private BillingDetails billingDetails;
  private Boolean archived;
}
