package fi.otavanopisto.pyramus.domainmodel.resources;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

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