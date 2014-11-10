package fi.pyramus.domainmodel.grading;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;

import fi.pyramus.domainmodel.base.ArchivableEntity;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.students.Student;

@Entity
public class CourseAssessmentRequest implements ArchivableEntity {
  
  public Long getId() {
    return id;
  }
  
  public void setCourseStudent(CourseStudent courseStudent) {
    this.courseStudent = courseStudent;
  }
  
  public CourseStudent getCourseStudent() {
    return courseStudent;
  }

  @Transient
  public Student getStudent() {
    return courseStudent != null ? courseStudent.getStudent() : null;
  }
  
  public String getRequestText() {
    return requestText;
  }

  public void setRequestText(String requestText) {
    this.requestText = requestText;
  }

  @Override
  public Boolean getArchived() {
    return archived;
  }

  @Override
  public void setArchived(Boolean archived) {
    this.archived = archived;
  }
  
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseAssessmentRequest")  
  @TableGenerator(name="CourseAssessmentRequest", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name="courseStudent")
  private CourseStudent courseStudent;

  @Column (nullable=false)
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date created;
  
  @Lob
  @Basic (fetch = FetchType.LAZY)
  @Column
  private String requestText;

  @NotNull
  @Column (nullable = false)
  @Field
  private Boolean archived = Boolean.FALSE;
}