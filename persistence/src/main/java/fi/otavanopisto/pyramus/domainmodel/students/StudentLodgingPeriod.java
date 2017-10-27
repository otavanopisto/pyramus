package fi.otavanopisto.pyramus.domainmodel.students;

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
import javax.validation.constraints.NotNull;

@Entity
public class StudentLodgingPeriod {
  
  public Long getId() {
    return id;
  }
  
  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Date getBegin() {
    return begin;
  }

  public void setBegin(Date begin) {
    this.begin = begin;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id; 
  
  @ManyToOne (optional = false)
  @JoinColumn (name = "student")
  private Student student;
  
  @NotNull
  @Column (nullable = false)
  @Temporal (value=TemporalType.DATE)
  private Date begin;

  @Temporal (value=TemporalType.DATE)
  private Date end;
}
