package fi.otavanopisto.pyramus.domainmodel.changelog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.NotEmpty;

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
