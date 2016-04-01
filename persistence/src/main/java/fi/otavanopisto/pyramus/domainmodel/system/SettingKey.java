package fi.otavanopisto.pyramus.domainmodel.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: Setting
 */
@Entity
public class SettingKey {

  public Long getId() {
    return id;
  } 
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="SettingKey")  
  @TableGenerator(name="SettingKey", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @Column (nullable = false, unique = true)
  @NotEmpty
  private String name;
}
