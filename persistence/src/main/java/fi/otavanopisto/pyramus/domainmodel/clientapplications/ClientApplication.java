package fi.otavanopisto.pyramus.domainmodel.clientapplications;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
  
  public Boolean getSkipPrompt() {
    return skipPrompt;
  }

  public void setSkipPrompt(Boolean skipPrompt) {
    this.skipPrompt = skipPrompt;
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
  
  @NotNull
  @Column (nullable = false)
  private Boolean skipPrompt;
  
}
