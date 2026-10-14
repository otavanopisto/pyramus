package fi.otavanopisto.pyramus.domainmodel.matriculation;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentFlag;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "person_id", "flag"})
    }
)
public class MatriculationPersonFlag {

  public Long getId() {
    return id;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public MatriculationExamEnrollmentFlag getFlag() {
    return flag;
  }

  public void setFlag(MatriculationExamEnrollmentFlag flag) {
    this.flag = flag;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne
  private Person person;

  @Enumerated (EnumType.STRING)
  private MatriculationExamEnrollmentFlag flag;
  
}
