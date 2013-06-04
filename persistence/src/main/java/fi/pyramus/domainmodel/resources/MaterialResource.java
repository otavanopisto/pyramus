package fi.pyramus.domainmodel.resources;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

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
  @Type (type="MonetaryAmount")  
  private MonetaryAmount unitCost;
}
