package fi.otavanopisto.pyramus.domainmodel.changelog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class TrackedEntityProperty {

  public Long getId() {
    return id;
  }
  
  public String getEntity() {
    return entity;
  }
  
  public void setEntity(String entity) {
    this.entity = entity;
  }
  
  public String getProperty() {
    return property;
  }
  
  public void setProperty(String property) {
    this.property = property;
  }
  
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="TrackedEntityProperty")  
  @TableGenerator(name="TrackedEntityProperty", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotEmpty
  private String entity;
  
  @NotEmpty
  private String property;
}
