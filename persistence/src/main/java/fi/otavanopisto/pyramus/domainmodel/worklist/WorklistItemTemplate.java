package fi.otavanopisto.pyramus.domainmodel.worklist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;

@Entity
@Indexed
public class WorklistItemTemplate implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Double getFactor() {
    return factor;
  }

  public void setFactor(Double factor) {
    this.factor = factor;
  }

  public WorklistItemTemplateType getTemplateType() {
    return templateType;
  }

  public void setTemplateType(WorklistItemTemplateType templateType) {
    this.templateType = templateType;
  }

  public Boolean getRemovable() {
    return removable;
  }

  public void setRemovable(Boolean removable) {
    this.removable = removable;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getEditableFields() {
    return editableFields;
  }

  public void setEditableFields(String editableFields) {
    this.editableFields = editableFields;
  }

  public String getBillingNumber() {
    return billingNumber;
  }

  public void setBillingNumber(String billingNumber) {
    this.billingNumber = billingNumber;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String description;

  @Column
  private Double price;

  @Column
  private Double factor;
  
  @Column
  private String billingNumber;

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private WorklistItemTemplateType templateType;
  
  @Column
  private String editableFields;
  
  @NotNull
  @Column(nullable = false)
  private Boolean removable = Boolean.FALSE;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

}
