package fi.otavanopisto.pyramus.domainmodel.clientapplications;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
  
  public boolean getSkipPrompt() {
    return skipPrompt;
  }

  public void setSkipPrompt(boolean skipPrompt) {
    this.skipPrompt = skipPrompt;
  }

  /**
   * Returns the scopes available to the users
   * logging in via this ClientApplication.
   * 
   * ClientApplications shall not be allowed to
   * request scopes that are not in this list.
   * 
   * @return
   */
  public Set<String> getScopes() {
    return scopes;
  }

  public void setScopes(Set<String> allowedScopes) {
    this.scopes = allowedScopes;
  }

  @Transient
  public boolean isAllowedRedirectURI(String redirectURI) {
    return redirectURIs != null ? redirectURIs.contains(redirectURI) : false;
  }
  
  @Transient
  public boolean isAllowedRedirectURI(URI redirectURI) {
    return isAllowedRedirectURI(redirectURI.toString());
  }
  
  public Set<String> getRedirectURIs() {
    return redirectURIs;
  }

  public void setRedirectURIs(Set<String> redirectURIs) {
    this.redirectURIs = redirectURIs;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isAllowAllRedirectURIs() {
    return allowAllRedirectURIs;
  }

  public void setAllowAllRedirectURIs(boolean allowAllRedirectURIs) {
    this.allowAllRedirectURIs = allowAllRedirectURIs;
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
  private boolean active;
  
  @NotNull
  @Column (nullable = false)
  private boolean skipPrompt;
  
  @ElementCollection
  @CollectionTable(name = "ClientApplicationScopes", joinColumns = @JoinColumn(name = "clientApplication"))
  @Column(name = "scope", nullable = false)
  private Set<String> scopes = new HashSet<>();

  @NotNull
  @Column (nullable = false)
  private boolean allowAllRedirectURIs;
  
  @ElementCollection
  @CollectionTable(name = "ClientApplicationAllowedRedirectURIs", joinColumns = @JoinColumn(name = "clientApplication"))
  @Column(name = "redirectURI", nullable = false)
  private Set<String> redirectURIs = new HashSet<>();
}
