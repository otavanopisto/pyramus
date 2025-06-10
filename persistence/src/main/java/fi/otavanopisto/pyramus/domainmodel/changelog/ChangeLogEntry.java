package fi.otavanopisto.pyramus.domainmodel.changelog;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class ChangeLogEntry {

  public Long getId() {
    return id;
  }
  
  public ChangeLogEntryType getType() {
    return type;
  }

  public void setType(ChangeLogEntryType type) {
    this.type = type;
  }
  
  public ChangeLogEntryEntity getEntity() {
    return entity;
  }

  public void setEntity(ChangeLogEntryEntity entity) {
    this.entity = entity;
  }

  public String getEntityId() {
    return entityId;
  }
  
  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }
  
  public Date getTime() {
    return time;
  }

  public User getUser() {
    return user;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ChangeLogEntry")  
  @TableGenerator(name="ChangeLogEntry", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private ChangeLogEntryType type;

  @ManyToOne  
  @JoinColumn(name="entity")
  private ChangeLogEntryEntity entity;

  @NotEmpty
  private String entityId;

  private Date time;

  @ManyToOne  
  @JoinColumn(name="user")
  private User user;
}
