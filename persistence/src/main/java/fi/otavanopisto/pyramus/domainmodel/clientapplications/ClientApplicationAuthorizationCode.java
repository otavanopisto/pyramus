package fi.otavanopisto.pyramus.domainmodel.clientapplications;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class ClientApplicationAuthorizationCode {

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public ClientApplication getClientApplication() {
    return clientApplication;
  }

  public void setClientApplication(ClientApplication clientApplication) {
    this.clientApplication = clientApplication;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }
  
  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ClientApplicationAuthorizationCode")
  @TableGenerator(name = "ClientApplicationAuthorizationCode", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @NotNull
  @ManyToOne
  @JoinColumn(name = "app_id", nullable = false)
  private ClientApplication clientApplication;

  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String authorizationCode;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String redirectUrl;

}
