package fi.otavanopisto.pyramus.domainmodel.grading;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: UserVariable
 *
 */
@Entity
public class CreditVariable {

  public CreditVariable() {
    super();
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Credit getCredit() {
    return credit;
  }
  
  public void setCredit(Credit credit) {
    this.credit = credit;
  }
  
  public CreditVariableKey getKey() {
    return key;
  }
  
  public void setKey(CreditVariableKey key) {
    this.key = key;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CreditVariable")  
  @TableGenerator(name="CreditVariable", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "credit")
  private Credit credit;
  
  @ManyToOne
  @JoinColumn(name = "variableKey")
  private CreditVariableKey key;
  
  @NotEmpty
  @Column (nullable = false)
  @NotNull
  @Lob
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
