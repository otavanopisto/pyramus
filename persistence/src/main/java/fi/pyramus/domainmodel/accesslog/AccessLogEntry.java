package fi.pyramus.domainmodel.accesslog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.users.User;

@Entity
public class AccessLogEntry {
  
  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public AccessLogEntryPath getPath() {
    return path;
  }

  public void setPath(AccessLogEntryPath path) {
    this.path = path;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="AccessLogEntry")  
  @TableGenerator(name="AccessLogEntry", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "user")
  private User user;

  @NotNull
  @Column (nullable = false)
  @NotEmpty
  private String ip;

  @NotNull
  @Column (updatable=false, nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date date;
  
  @ManyToOne
  @JoinColumn (name = "path")
  private AccessLogEntryPath path;

  @Lob
  private String parameters;
}
