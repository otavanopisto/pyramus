package fi.otavanopisto.pyramus.domainmodel.worklist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

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
  
  @Transient
  public boolean isUserCreatable() {
    return templateType == WorklistItemTemplateType.EDITABLE || templateType == WorklistItemTemplateType.UNEDITABLE;
  }

  @Transient
  public boolean isUserEditable() {
    return templateType == WorklistItemTemplateType.EDITABLE;
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
  
  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private WorklistItemTemplateType templateType;
  
  @NotNull
  @Column(nullable = false)
  private Boolean removable = Boolean.FALSE;

  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

}
