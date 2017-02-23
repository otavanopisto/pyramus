package fi.otavanopisto.pyramus.domainmodel.base;

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
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class LoginLog {

  public Long getId() {
    return id;
  }
  
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
  
  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="LoginLog")
  @TableGenerator(name="LoginLog", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn (name = "user")
  private User user;  
  
  @NotNull
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date date;
}
