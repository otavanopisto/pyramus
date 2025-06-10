package fi.otavanopisto.pyramus.domainmodel.grading;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;

import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;

@Entity
public class TransferCreditTemplate {
  
  public Long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }
  
  public List<TransferCreditTemplateCourse> getCourses() {
    return courses;
  }
  
  public void setCourses(List<TransferCreditTemplateCourse> courses) {
    this.courses = courses;
  }
  
  public void addCourse(TransferCreditTemplateCourse course) {
    if (!courses.contains(course)) {
      if (course.getTransferCreditTemplate() != null) {
        course.getTransferCreditTemplate().removeCourse(course);
      }
      
      course.setTransferCreditTemplate(this);
      courses.add(course);
    } else {
      throw new PersistenceException("Template already contains this course");
    }
  }
  
  public void removeCourse(TransferCreditTemplateCourse course) {
    if (courses.contains(course)) {
      course.setTransferCreditTemplate(null);
      courses.remove(course);
    } else {
      throw new PersistenceException("Template does not contain this course");
    }
  } 

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
  }

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="TransferCreditTemplate")  
  @TableGenerator(name="TransferCreditTemplate", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @NotEmpty
  @Column(nullable = false)
  private String name;
  
  @Version
  @Column(nullable = false)
  private Long version;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="transferCreditTemplate")
  private List<TransferCreditTemplateCourse> courses = new ArrayList<>();

  @ManyToOne
  private Curriculum curriculum;
}
