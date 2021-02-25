package fi.otavanopisto.pyramus.domainmodel.base;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Organization implements ArchivableEntity {
  
  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Boolean getArchived() {
    return archived;
  }

  public BillingDetails getBillingDetails() {
    return billingDetails;
  }

  public void setBillingDetails(BillingDetails billingDetails) {
    this.billingDetails = billingDetails;
  }

  public EducationType getEducationType() {
    return educationType;
  }

  public void setEducationType(EducationType educationType) {
    this.educationType = educationType;
  }

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String name;
  
  @ManyToOne
  @JoinColumn (name = "educationType")
  private EducationType educationType;
  
  @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name = "billingDetails")
  private BillingDetails billingDetails;
  
  @NotNull
  @Column (nullable = false)
  private Boolean archived;
}