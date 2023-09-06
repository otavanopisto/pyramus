package fi.otavanopisto.pyramus.domainmodel.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.Person;

@Entity
public class PersonVariable {

  public Long getId() {
    return id;
  }

  public PersonVariableKey getKey() {
    return key;
  }

  public void setKey(PersonVariableKey key) {
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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "PersonVariable")
  @TableGenerator(name = "PersonVariable", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "person")
  private Person person;

  @ManyToOne
  @JoinColumn(name = "variableKey")
  private PersonVariableKey key;

  @NotEmpty
  private String value;

  @Version
  @Column(nullable = false)
  private Long version;
}
