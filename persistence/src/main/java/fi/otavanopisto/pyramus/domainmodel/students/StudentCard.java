package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class StudentCard {

  public Long getId() {
    return id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public StudentCardType getType() {
    return type;
  }

  public void setType(StudentCardType type) {
    this.type = type;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }


  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY) 
  private Long id;
  
  @NotNull
  @OneToOne 
  private Student student;
  
  @Column
  @Enumerated (EnumType.STRING)
  private StudentCardType type;

  @NotNull
  @Column(nullable = false)
  private Boolean active = false;
  
  @NotNull
  @Temporal(value = TemporalType.DATE)
  @Column
  private Date expiryDate;
}
