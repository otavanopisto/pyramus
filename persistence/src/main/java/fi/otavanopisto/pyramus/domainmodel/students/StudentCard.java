package fi.otavanopisto.pyramus.domainmodel.students;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class StudentCard {

  public Long getId() {
    return id;
  }

  public Long getStudent() {
    return userSchoolDataIdentifier_id;
  }

  public void setStudent(Long userSchoolDataIdentifier_id) {
    this.userSchoolDataIdentifier_id = userSchoolDataIdentifier_id;
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
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentCard")  
  @TableGenerator(name="StudentCard", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @NotNull
  @Column (nullable = false)
  private Long userSchoolDataIdentifier_id;
  
  @NotNull
  @Column(nullable = false)
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
