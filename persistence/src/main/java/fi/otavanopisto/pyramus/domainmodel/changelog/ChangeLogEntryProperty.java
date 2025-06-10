package fi.otavanopisto.pyramus.domainmodel.changelog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;

@Entity
public class ChangeLogEntryProperty {

  public Long getId() {
    return id;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public ChangeLogEntryEntityProperty getProperty() {
    return property;
  }
  
  public void setProperty(ChangeLogEntryEntityProperty property) {
    this.property = property;
  }
  
  public ChangeLogEntry getEntry() {
    return entry;
  }
  
  public void setEntry(ChangeLogEntry entry) {
    this.entry = entry;
  }
  
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ChangeLogEntryProperty")  
  @TableGenerator(name="ChangeLogEntryProperty", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne  
  @JoinColumn(name="property")
  private ChangeLogEntryEntityProperty property;

  @ManyToOne  
  @JoinColumn(name="entry")
  private ChangeLogEntry entry;
  
  @Lob
  private String value;
}
