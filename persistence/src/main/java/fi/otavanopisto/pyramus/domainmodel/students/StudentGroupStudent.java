package fi.otavanopisto.pyramus.domainmodel.students;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;

@Entity
public class StudentGroupStudent {

  /**
   * Returns unique identifier for this StudentGroupStudent
   * 
   * @return unique id of this StudentGroupStudent
   */
  public Long getId() {
    return id;
  }
  
  protected void setStudentGroup(StudentGroup studentGroup) {
    this.studentGroup = studentGroup;
  }

  public StudentGroup getStudentGroup() {
    return studentGroup;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Student getStudent() {
    return student;
  }

  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentGroupStudent")  
  @TableGenerator(name="StudentGroupStudent", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  @DocumentId
  private Long id;
  
  @OneToOne
  @JoinColumn (name = "studentGroup")
  private StudentGroup studentGroup;
  
  @ManyToOne
  @JoinColumn (name = "student")
  private Student student;

  @Version
  @Column(nullable = false)
  private Long version;
}
