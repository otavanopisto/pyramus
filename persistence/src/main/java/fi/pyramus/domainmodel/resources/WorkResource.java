package fi.pyramus.domainmodel.resources;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Indexed;

import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class WorkResource extends Resource {
  
  public MonetaryAmount getHourlyCost() {
    return hourlyCost;
  }
  
  public void setHourlyCost(MonetaryAmount hourlyCost) {
    this.hourlyCost = hourlyCost;
  }
  
  public MonetaryAmount getCostPerUse() {
    return costPerUse;
  }
  
  public void setCostPerUse(MonetaryAmount costPerUse) {
    this.costPerUse = costPerUse;
  }
  
  @Override
  @Transient
  public ResourceType getResourceType() {
    return ResourceType.WORK_RESOURCE;
  }
  
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="hourlyCost_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="hourlyCost_currency"))
  })
  private MonetaryAmount hourlyCost;
  
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="costPerUse_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="costPerUse_currency"))
  })
  private MonetaryAmount costPerUse;
}