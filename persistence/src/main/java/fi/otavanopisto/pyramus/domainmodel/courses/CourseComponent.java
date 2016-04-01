package fi.otavanopisto.pyramus.domainmodel.courses;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.PrimaryKeyJoinColumn;

import fi.otavanopisto.pyramus.domainmodel.base.ComponentBase;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CourseComponent extends ComponentBase {

  public Course getCourse() {
    return course;
  }
  
  public void setCourse(Course course) {
    this.course = course;
  }
  
  public List<CourseComponentResource> getResources() {
    return resources;
  }
  
  @SuppressWarnings("unused")
  private void setResources(List<CourseComponentResource> resource) {
    this.resources = resource;
  }
  
  public void addResource(CourseComponentResource resource) {
    if (!resources.contains(resource)) {
      if (resource.getCourseComponent() != null)
        resource.getCourseComponent().removeResource(resource);
      
      resource.setCourseComponent(this);
      resources.add(resource);
    } else {
      throw new PersistenceException("Component already contains this resource");
    }
  }
  
  public void removeResource(CourseComponentResource resource) {
    if (resources.contains(resource)) {
      resource.setCourseComponent(null);
      this.resources.remove(resource);
    } else {
      throw new PersistenceException("Component does not contain this resource");
    }
  } 
  
  @ManyToOne
  @JoinColumn(name="course")
  private Course course;

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="courseComponent")
  private List<CourseComponentResource> resources = new ArrayList<CourseComponentResource>();
}
