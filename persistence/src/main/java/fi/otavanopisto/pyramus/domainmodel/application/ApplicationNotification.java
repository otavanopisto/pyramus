package fi.otavanopisto.pyramus.domainmodel.application;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class ApplicationNotification {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public ApplicationState getState() {
    return state;
  }

  public void setState(ApplicationState state) {
    this.state = state;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="ApplicationNotification")  
  @TableGenerator(name="ApplicationNotification", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  private String line;

  @Column
  @Enumerated (EnumType.STRING)
  private ApplicationState state;
  
  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name = "__ApplicationNotificationUsers", joinColumns = @JoinColumn(name = "notification"), inverseJoinColumns = @JoinColumn(name = "user"))
  private Set<User> users = new HashSet<>();

}