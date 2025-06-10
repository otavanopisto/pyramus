package fi.otavanopisto.pyramus.domainmodel.application;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.TableGenerator;

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

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
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