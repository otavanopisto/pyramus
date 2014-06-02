package fi.pyramus.domainmodel.plugins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class PluginRepository {

  /**
   * Returns internal unique id.
   * 
   * @return internal unique id
   */
  public Long getId() {
    return id;
  }
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }

  public String getRepositoryId() {
    return repositoryId;
  }

  public void setRepositoryId(String repositoryId) {
    this.repositoryId = repositoryId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "PluginRepository")
  @TableGenerator(name = "PluginRepository", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String url;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String repositoryId;
}
