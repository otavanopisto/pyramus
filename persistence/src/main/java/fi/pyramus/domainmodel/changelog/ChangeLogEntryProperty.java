package fi.pyramus.domainmodel.changelog;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;


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
  
  @Column (length=1073741824)
  private String value;
}
