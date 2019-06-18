package fi.otavanopisto.pyramus.domainmodel.system;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: Configuration
 */
@Entity
public class Configuration {

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "Configuration")
  @TableGenerator(name = "Configuration", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String name;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column
  private String value;

}
