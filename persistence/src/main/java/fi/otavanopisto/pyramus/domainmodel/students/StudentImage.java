package fi.otavanopisto.pyramus.domainmodel.students;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;

@Entity
public class StudentImage {

  /**
   * Returns internal unique id
   * 
   * @return Internal unique id
   */
  public Long getId() {
    return id;
  }
  
  public void setData(byte[] data) {
    this.data = data;
  }

  public byte[] getData() {
    return data;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentType() {
    return contentType;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Student getStudent() {
    return student;
  }

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentImage")  
  @TableGenerator(name="StudentImage", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @OneToOne
  @JoinColumn(name="student")
  private Student student;
  
  private String contentType;
  
  @Lob
  private byte[] data;
}
