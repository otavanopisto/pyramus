package fi.otavanopisto.pyramus.domainmodel.grading;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
  
  @Basic(optional = false)
  @NotEmpty
  @NotNull
  @Lob
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
