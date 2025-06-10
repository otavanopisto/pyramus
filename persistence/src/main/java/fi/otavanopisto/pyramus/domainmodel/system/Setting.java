package fi.otavanopisto.pyramus.domainmodel.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;

/**
 * Entity implementation class for Entity: Setting
 */
@Entity
public class Setting {

  public Long getId() {
    return this.id;
  }
  
  public SettingKey getKey() {
    return key;
  }
  
  public void setKey(SettingKey key) {
    this.key = key;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="Setting")  
  @TableGenerator(name="Setting", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "settingKey")
  private SettingKey key;
  
  @NotEmpty
  private String value;
}
