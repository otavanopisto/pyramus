package fi.pyramus.domainmodel.grading;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotEmpty;

import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.Subject;

@Entity
@Indexed
public class TransferCreditTemplateCourse {
  
  public Long getId() {
    return id;
  }
  
  public TransferCreditTemplate getTransferCreditTemplate() {
    return transferCreditTemplate;
  }
  
  public void setTransferCreditTemplate(TransferCreditTemplate transferCreditTemplate) {
    this.transferCreditTemplate = transferCreditTemplate;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }
  
  public Integer getCourseNumber() {
    return courseNumber;
  }
  
  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public EducationalLength getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(EducationalLength courseLength) {
    this.courseLength = courseLength;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }
  
  public CourseOptionality getOptionality() {
    return optionality;
  }
  
  public void setOptionality(CourseOptionality optionality) {
    this.optionality = optionality;
  }

  @Id 
  @DocumentId
  @GeneratedValue(strategy=GenerationType.TABLE, generator="TransferCreditTemplateCourse")  
  @TableGenerator(name="TransferCreditTemplateCourse", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "transferCreditTemplate")
  private TransferCreditTemplate transferCreditTemplate;
  
  @NotNull
  @Column(nullable = false)
  @NotEmpty
  @Field
  private String courseName;

  private Integer courseNumber;
  
  @OneToOne
  @JoinColumn(name = "courseLength")
  private EducationalLength courseLength;
  
  @NotNull
  @Column (nullable = false)
  @Enumerated (EnumType.STRING)
  private CourseOptionality optionality;

  @ManyToOne
  @JoinColumn(name = "subject")
  private Subject subject;
  
  @Version
  @Column(nullable = false)
  private Long version;
}
