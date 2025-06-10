package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

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
  
  public Boolean getHandled() {
    return handled;
  }
  
  public void setHandled(Boolean handled) {
    this.handled = handled;
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
  private String requestText;

  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean handled = Boolean.FALSE;
  
  @NotNull
  @Column (nullable = false)
  @GenericField
  private Boolean archived = Boolean.FALSE;
}