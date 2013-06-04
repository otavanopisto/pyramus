package fi.pyramus.domainmodel.resources;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.search.annotations.Indexed;

import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.pyramus.persistence.usertypes.MonetaryAmountUserType;

@Entity
@TypeDefs ({
  @TypeDef (name="MonetaryAmount", typeClass=MonetaryAmountUserType.class)
})
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
  
  @Type (type="MonetaryAmount")  
  private MonetaryAmount hourlyCost;
  
  @Type (type="MonetaryAmount")  
  private MonetaryAmount costPerUse;
}