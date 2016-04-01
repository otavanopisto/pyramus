package fi.otavanopisto.pyramus.domainmodel.courses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

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
