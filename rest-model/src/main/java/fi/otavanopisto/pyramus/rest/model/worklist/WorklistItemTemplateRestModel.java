package fi.otavanopisto.pyramus.rest.model.worklist;

import java.util.Set;

/**
 * Representation of a worklist item template.
 */
public class WorklistItemTemplateRestModel {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Set<String> getEditableFields() {
    return editableFields;
  }

  public void setEditableFields(Set<String> editableFields) {
    this.editableFields = editableFields;
  }

  private Long id;
  private String description;
  private Double price;
  private Double factor;
  private Set<String> editableFields;

}
