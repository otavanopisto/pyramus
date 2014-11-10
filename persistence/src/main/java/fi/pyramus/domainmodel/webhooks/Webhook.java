package fi.pyramus.domainmodel.webhooks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Webhook {

  public Long getId() {
    return id;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public String getUrl() {
    return url;
  }
  
  public String getSecret() {
    return secret;
  }
  
  public void setSecret(String secret) {
    this.secret = secret;
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "Webhook")
  @TableGenerator(name = "Webhook", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String url;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String secret;
}
