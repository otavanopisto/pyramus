package fi.otavanopisto.pyramus.domainmodel.resources;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.persistence.usertypes.MonetaryAmount;

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
