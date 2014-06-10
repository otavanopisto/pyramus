package fi.pyramus.domainmodel.accesslog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Indexed
public class AccessLogEntryPath {
  
  public Long getId() {
    return id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="AccessLogEntryPath")  
  @TableGenerator(name="AccessLogEntryPath", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String path;
}
