package fi.otavanopisto.pyramus.domainmodel.clientapplications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class ClientApplicationAccessToken {

  public Long getId() {
    return id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

  public ClientApplication getClientApplication() {
    return clientApplication;
  }

  public void setClientApplication(ClientApplication clientApplication) {
    this.clientApplication = clientApplication;
  }
  
  public ClientApplicationAuthorizationCode getClientApplicationAuthorizationCode() {
    return clientApplicationAuthorizationCode;
  }

  public void setClientApplicationAuthorizationCode(ClientApplicationAuthorizationCode clientApplicationAuthorizationCode) {
    this.clientApplicationAuthorizationCode = clientApplicationAuthorizationCode;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ClientApplicationAccessToken")
  @TableGenerator(name = "ClientApplicationAccessToken", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique=true)
  private String accessToken;

  @NotEmpty
  @Column(nullable = false)
  private String refreshToken;
  
  @NotNull
  @Column(nullable = false)
  private Long expires;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "app_id", nullable = false)
  private ClientApplication clientApplication;
  
  @NotNull
  @OneToOne
  @JoinColumn(name = "clientApplicationAuthorizationCode", unique = true, nullable = false)
  private ClientApplicationAuthorizationCode clientApplicationAuthorizationCode;

}
