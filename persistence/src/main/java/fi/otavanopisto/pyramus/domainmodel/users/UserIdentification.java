package fi.otavanopisto.pyramus.domainmodel.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.base.Person;

@Entity
public class UserIdentification {

  /**
   * Returns internal unique id
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  public String getExternalId() {
    return externalId;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getAuthSource() {
    return authSource;
  }

  public void setAuthSource(String authSource) {
    this.authSource = authSource;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="UserIdentification")  
  @TableGenerator(name="UserIdentification", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  private Person person;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String externalId;

  @NotNull
  @Column(nullable = false)
  @NotEmpty
  private String authSource;

}
