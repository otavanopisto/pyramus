package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

  public StudentCardActivity getActivity() {
    return activity;
  }

  public void setActivity(StudentCardActivity activity) {
    this.activity = activity;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }
  
  public Date getCancellationDate() {
    return cancellationDate;
  }

  public void setCancellationDate(Date cancellationDate) {
    this.cancellationDate = cancellationDate;
  }

  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY) 
  private Long id;
  
  @OneToOne 
  @JoinColumn(name="student", unique = true)
  private Student student;
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private StudentCardType type;

  @NotNull
  @Column(nullable = false)
  @Enumerated (EnumType.STRING)
  private StudentCardActivity activity;
  
  @NotNull
  @Temporal(value = TemporalType.DATE)
  @Column (nullable = false)
  private Date expiryDate;
  
  @Temporal(value = TemporalType.DATE)
  private Date cancellationDate;
}
