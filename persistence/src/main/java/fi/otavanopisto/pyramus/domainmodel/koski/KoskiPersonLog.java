package fi.otavanopisto.pyramus.domainmodel.koski;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Entity
public class KoskiPersonLog {

  public Long getId() {
    return id;
  }
  
  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public KoskiPersonState getState() {
    return state;
  }

  public void setState(KoskiPersonState state) {
    this.state = state;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id; 

  @ManyToOne (optional = false)
  @JoinColumn (name = "person")
  private Person person;
  
  @ManyToOne
  @JoinColumn (name = "student")
  private Student student;
  
  @NotNull
  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date date;
  
  @Enumerated (EnumType.STRING)
  private KoskiPersonState state;
  
  @Lob
  @Column
  private String message;
  
}
