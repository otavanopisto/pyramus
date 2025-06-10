package fi.otavanopisto.pyramus.domainmodel.courses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;

import fi.otavanopisto.pyramus.domainmodel.resources.Resource;

@Entity
public class CourseComponentResource {

  public Long getId() {
    return id;
  }
  
  public CourseComponent getCourseComponent() {
    return courseComponent;
  }
  
  public void setCourseComponent(CourseComponent courseComponent) {
    this.courseComponent = courseComponent;
  }
  
  public Resource getResource() {
    return resource;
  }
  
  public void setResource(Resource resource) {
    this.resource = resource;
  }
  
  public Double getUsagePercent() {
    return usagePercent;
  }
  
  public void setUsagePercent(Double usagePercent) {
    this.usagePercent = usagePercent;
  }
  
  @Id 
  @GeneratedValue(strategy=GenerationType.TABLE, generator="CourseComponentResource")  
  @TableGenerator(name="CourseComponentResource", allocationSize=1, table = "hibernate_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_next_hi_value")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "courseComponent")
  private CourseComponent courseComponent;
 
  @ManyToOne
  @JoinColumn (name="resource")
  private Resource resource;
  
  @NotNull
  @Column (nullable = false)
  private Double usagePercent;

}
