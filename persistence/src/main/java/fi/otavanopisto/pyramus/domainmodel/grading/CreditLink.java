package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Entity
public class CreditLink implements ArchivableEntity {

  public Long getId() {
    return id;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Credit getCredit() {
    return credit;
  }

  public void setCredit(Credit credit) {
    this.credit = credit;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CreditLink")  
  @TableGenerator(name="CreditLink", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name="credit")
  private Credit credit;
  
  @ManyToOne
  @JoinColumn(name="student")
  private Student student;

  @Basic (optional = false)
  private Boolean archived = Boolean.FALSE;
  
  @ManyToOne  
  @JoinColumn(name="creator")
  private User creator;
  
  @Temporal (TemporalType.TIMESTAMP)
  private Date created;
  
}
