package fi.otavanopisto.pyramus.domainmodel.users;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
public class StudentParentInvitation {

  /**
   * Returns internal unique id
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns true if this invitation is considered expired.
   * 
   * Expiration is hard set at two weeks.
   * 
   * @return true if this invitation is considered expired.
   */
  @Transient
  public boolean isExpired() {
    if (created == null) {
      return true;
    }

    LocalDateTime createdLD = Instant.ofEpochMilli(created.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    return LocalDateTime.now().minusWeeks(2).isAfter(createdLD);
  }
  
  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn (nullable = false)
  @ManyToOne
  private Student student;

  @Column (nullable = false)
  @NotEmpty
  @NotNull
  private String firstName;
  
  @Column (nullable = false)
  @NotEmpty
  @NotNull
  private String lastName;
  
  @Column (nullable = false)
  @NotEmpty
  @NotNull
  @Email
  private String email;
  
  @Column (nullable = false, unique = true)
  @NotEmpty
  @NotNull
  private String hash;

  @NotNull
  @Column (nullable = false)
  @Temporal (value = TemporalType.TIMESTAMP)
  private Date created;
  
}
