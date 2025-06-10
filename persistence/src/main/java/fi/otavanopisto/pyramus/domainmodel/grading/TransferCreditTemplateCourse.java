package fi.otavanopisto.pyramus.domainmodel.grading;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;

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

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
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
  @FullTextField
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
  
  @ManyToOne
  private Curriculum curriculum;

  @Version
  @Column(nullable = false)
  private Long version;
}
