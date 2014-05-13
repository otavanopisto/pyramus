package fi.pyramus.domainmodel.courses;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.persistence.usertypes.MonetaryAmount;

@Entity
public class GradeCourseResource {
  
  protected GradeCourseResource() {
  }
  
  public Long getId() {
    return id;
  }

  public GradeCourseResource(Course course, Resource resource) {
    setCourse(course);
    setResource(resource);    
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
    // TODO Course will some fine day tell us the number of students with a grade, yes? :)
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

  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="GradeCourseResource")  
  @TableGenerator(name="GradeCourseResource", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;
 
  @ManyToOne
  @JoinColumn (name="resource")
  private Resource resource;
  
  @NotNull
  @Column (nullable = false)
  private Double hours;
  
  @NotNull
  @Column (nullable = false)
  @Embedded  
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="hourlyCost_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="hourlyCost_currency"))
  })
  private MonetaryAmount hourlyCost;
  
  @NotNull
  @Column (nullable = false)
  @Embedded  
  @AttributeOverrides({
    @AttributeOverride(name="amount", column = @Column(name="unitCost_amount") ),
    @AttributeOverride(name="currency", column = @Column(name="unitCost_currency"))
  })
  private MonetaryAmount unitCost;

  @Version
  @Column(nullable = false)
  private Long version;
}