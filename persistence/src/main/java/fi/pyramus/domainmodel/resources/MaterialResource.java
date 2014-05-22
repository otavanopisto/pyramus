package fi.pyramus.domainmodel.resources;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;

import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
public class MaterialResource extends Resource {

  public MonetaryAmount getUnitCost() {
    return unitCost;
  }
  
  public void setUnitCost(MonetaryAmount unitCost) {
    this.unitCost = unitCost;
  }
  
  @Override
  @Transient
  public ResourceType getResourceType() {
    return ResourceType.MATERIAL_RESOURCE;
  }
  
  @NotNull
  @Column (nullable = false)
  @Embedded  
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="unitCost_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="unitCost_currency"))
  })
  private MonetaryAmount unitCost;
}
