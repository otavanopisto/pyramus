package fi.otavanopisto.pyramus.domainmodel.migration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class ContactInfoMigrationUser {

  /**
   * Returns the unique identifier of this object.
   * 
   * @return The unique identifier of this object
   */
  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)  
  private Long id;

  @OneToOne
  private User user;
}