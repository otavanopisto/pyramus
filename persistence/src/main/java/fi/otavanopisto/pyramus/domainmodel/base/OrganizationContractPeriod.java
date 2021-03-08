package fi.otavanopisto.pyramus.domainmodel.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class OrganizationContractPeriod {
  
  public Long getId() {
    return id;
  }
  
  public Date getBegin() {
    return begin;
  }

  public void setBegin(Date begin) {
    this.begin = begin;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id; 
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "organization")
  private Organization organization;
  
  @NotNull
  @Column (nullable = false)
  @Temporal (value=TemporalType.DATE)
  private Date begin;

  @Temporal (value=TemporalType.DATE)
  private Date end;
}
