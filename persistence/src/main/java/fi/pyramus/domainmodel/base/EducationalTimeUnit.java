package fi.pyramus.domainmodel.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class EducationalTimeUnit implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getBaseUnits() {
    return baseUnits;
  }

  public void setBaseUnits(Double baseUnits) {
    this.baseUnits = baseUnits;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }
  
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  public String getSymbol() {
    return symbol;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="EducationalTimeUnit")  
  @TableGenerator(name="EducationalTimeUnit", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  private Double baseUnits;

  @NotNull
  @NotEmpty
  @Column (nullable = false)
  private String name;
  
  @NotNull
  @Column(nullable = false)
  private Boolean archived = Boolean.FALSE;

  @Version
  @Column(nullable = false)
  private Long version;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String symbol;
}
