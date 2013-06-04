package fi.pyramus.domainmodel.drafts;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.users.User;

@Entity
public class FormDraft {

  public Long getId() {
    return id;
  }
  
  public String getUrl() {
    return url;
  }
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public Date getCreated() {
    return created;
  }
  
  public void setCreated(Date created) {
    this.created = created;
  }
  
  public User getCreator() {
    return creator;
  }
  
  public void setCreator(User creator) {
    this.creator = creator;
  }
  
  public Date getModified() {
    return modified;
  }
  
  public void setModified(Date modified) {
    this.modified = modified;
  }
  
  public String getData() {
    return data;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="FormDraft")  
  @TableGenerator(name="FormDraft", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotEmpty 
  private String url;
  
  @Column (length=1073741824)
  private String data;
  
  @ManyToOne  
  @JoinColumn(name="creator")
  private User creator;
  
  @Temporal (TemporalType.TIMESTAMP)
  private Date created;
  
  @Temporal (TemporalType.TIMESTAMP)
  private Date modified;

  @Version
  @Column(nullable = false)
  private Long version;
}
