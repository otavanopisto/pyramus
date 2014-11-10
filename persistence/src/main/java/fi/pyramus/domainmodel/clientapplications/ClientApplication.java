package fi.pyramus.domainmodel.clientapplications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class ClientApplication {

  public Long getId() {
    return id;
  }
  
  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }
  
  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ClientApplication")
  @TableGenerator(name = "ClientApplication", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String clientName;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique=true)
  private String clientId;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String clientSecret;
  
}
