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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
public class StudentStudyPeriod {
  
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

  public StudentStudyPeriodType getPeriodType() {
    return periodType;
  }

  public void setPeriodType(StudentStudyPeriodType periodType) {
    this.periodType = periodType;
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
  
  @Enumerated (EnumType.STRING)
  @Column (nullable = false)
  private StudentStudyPeriodType periodType;
}
