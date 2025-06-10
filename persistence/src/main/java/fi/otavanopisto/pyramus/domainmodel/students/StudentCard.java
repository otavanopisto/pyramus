package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

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
  
  @Column
  @Enumerated (EnumType.STRING)
  private StudentCardType type;

  @NotNull
  @Column(nullable = false)
  @Enumerated (EnumType.STRING)
  private StudentCardActivity activity;
  
  @Temporal(value = TemporalType.DATE)
  private Date expiryDate;
  
  @Temporal(value = TemporalType.DATE)
  private Date cancellationDate;
}
