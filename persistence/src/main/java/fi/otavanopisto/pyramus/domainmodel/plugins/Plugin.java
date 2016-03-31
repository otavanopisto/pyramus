package fi.otavanopisto.pyramus.domainmodel.plugins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Plugin {

  /**
   * Returns internal unique id.
   * 
   * @return internal unique id
   */
  public Long getId() {
    return id;
  }
  
  public String getGroupId() {
    return groupId;
  }
  
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
  
  public String getArtifactId() {
    return artifactId;
  }
  
  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }
  
  public String getVersion() {
    return version;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }

  public Boolean getEnabled() {
    return enabled;
  }
  
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "Plugin")
  @TableGenerator(name = "Plugin", allocationSize = 1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String groupId;

  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String artifactId;
  
  @NotNull
  @NotEmpty
  @Column(nullable = false)
  private String version;

  @NotNull
  @Column(nullable = false)
  private Boolean enabled;
}
