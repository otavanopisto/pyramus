package fi.otavanopisto.pyramus.domainmodel.clientapplications;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class ClientApplicationAuthorizationCode {

  public static final Duration AUTHCODE_LIFETIME = Duration.ofSeconds(60);

  /**
   * Returns true if this authorization code is not expired.
   * @return
   */
  @Transient
  public boolean isValidAuthorizationCode() {
    return Instant.now().minus(AUTHCODE_LIFETIME).isBefore(issuedAt);
  }
  
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

  public Set<String> getSelectedScopes() {
    return selectedScopes;
  }

  public void setSelectedScopes(Set<String> selectedScopes) {
    this.selectedScopes = selectedScopes;
  }

  public Instant getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(Instant issuedAt) {
    this.issuedAt = issuedAt;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  @Column(nullable = false, updatable = false)
  private Instant issuedAt;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String authorizationCode;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String redirectUrl;

  @ElementCollection(fetch = FetchType.EAGER) // Has to be eager, maybe figure out later
  @CollectionTable(name = "ClientApplicationAuthorizationCodeScopes", joinColumns = @JoinColumn(name = "authorizationCode"))
  @Column(name = "scope", nullable = false)
  private Set<String> selectedScopes = new HashSet<>();
}
