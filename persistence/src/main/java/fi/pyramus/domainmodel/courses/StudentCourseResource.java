package fi.pyramus.domainmodel.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;
import fi.pyramus.persistence.usertypes.MonetaryAmountUserType;

@Entity
@TypeDefs ({
  @TypeDef (name="MonetaryAmount", typeClass=MonetaryAmountUserType.class)
})
public class StudentCourseResource {
  
  protected StudentCourseResource() {
  }
  
  public StudentCourseResource(Course course, Resource resource) {
    setCourse(course);
    setResource(resource);    
  }
  
  public Long getId() {
    return id;
  }

  public Resource getResource() {
    return resource;
  }
  
  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Course getCourse() {
    return course;
  }
 
  public void setCourse(Course course) {
    this.course = course;
  }

  public void setHours(Double hours) {
    this.hours = hours;
  }

  public Double getHours() {
    return hours;
  }

  public void setHourlyCost(MonetaryAmount hourlyCost) {
    this.hourlyCost = hourlyCost;
  }

  public MonetaryAmount getHourlyCost() {
    return hourlyCost;
  }

  @Transient
  public Integer getUnits() {
    return course.getStudentCount();
  }

  public void setUnitCost(MonetaryAmount unitCost) {
    this.unitCost = unitCost;
  }

  public MonetaryAmount getUnitCost() {
    return unitCost;
  }
  
  @SuppressWarnings("unused")
  private void setVersion(Long version) {
    this.version = version;
  }

  public Long getVersion() {
    return version;
  }

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;
 
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="StudentCourseResource")  
  @TableGenerator(name="StudentCourseResource", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn (name="resource")
  private Resource resource;
  
  @NotNull
  @Column (nullable = false)
  private Double hours;
  
  @NotNull
  @Column (nullable = false)
  @Type (type="MonetaryAmount")  
  private MonetaryAmount hourlyCost;
  
  @NotNull
  @Column (nullable = false)
  @Type (type="MonetaryAmount")  
  private MonetaryAmount unitCost;

  @Version
  @Column(nullable = false)
  private Long version;
}