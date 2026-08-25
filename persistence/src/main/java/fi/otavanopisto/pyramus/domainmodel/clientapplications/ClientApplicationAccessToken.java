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
public class ClientApplicationAccessToken {

  public static final Duration ACCESSTOKEN_LIFETIME = Duration.ofSeconds(3600);
  public static final Duration REFRESHTOKEN_LIFETIME = Duration.ofDays(7);

  @Transient
  public boolean isValidAccessToken() {
    return Instant.now().minus(ACCESSTOKEN_LIFETIME).isBefore(accessTokenIssuedAt);
  }
  
  @Transient
  public boolean isValidRefreshToken() {
    return Instant.now().minus(REFRESHTOKEN_LIFETIME).isBefore(refreshTokenIssuedAt);
  }
  
  public Long getId() {
    return id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public ClientApplication getClientApplication() {
    return clientApplication;
  }

  public void setClientApplication(ClientApplication clientApplication) {
    this.clientApplication = clientApplication;
  }
  
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> scopes) {
    this.scopes = scopes;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Instant getAccessTokenIssuedAt() {
    return accessTokenIssuedAt;
  }

  public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
    this.accessTokenIssuedAt = accessTokenIssuedAt;
  }

  public Instant getRefreshTokenIssuedAt() {
    return refreshTokenIssuedAt;
  }

  public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
    this.refreshTokenIssuedAt = refreshTokenIssuedAt;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String accessToken;

  @NotEmpty
  @Column(nullable = false, unique = true)
  private String refreshToken;
  
  @NotNull
  @Column(nullable = false)
  private Instant accessTokenIssuedAt;

  @NotNull
  @Column(nullable = false, updatable = false)
  private Instant refreshTokenIssuedAt;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "app_id", nullable = false)
  private ClientApplication clientApplication;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @ElementCollection(fetch = FetchType.EAGER) // Has to be eager, maybe figure out later
  @CollectionTable(name = "ClientApplicationAccessTokenScopes", joinColumns = @JoinColumn(name = "token"))
  @Column(name = "scope", nullable = false)
  private Set<String> scopes = new HashSet<>();
  
}
