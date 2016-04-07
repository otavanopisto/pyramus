package fi.otavanopisto.pyramus.domainmodel.courses;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceException;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.persistence.search.filters.ArchivedEntityFilterFactory;
import fi.otavanopisto.security.ContextReference;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
@FullTextFilterDefs (
  @FullTextFilterDef (
     name="ArchivedCourse",
     impl=ArchivedEntityFilterFactory.class
  )
)
public class Course extends CourseBase implements ArchivableEntity, ContextReference {

  public void setModule(Module module) {
    this.module = module;
  }

  public Module getModule() {
    return module;
  }

  public List<CourseComponent> getCourseComponents() {
    return courseComponents;
  }
  
  @SuppressWarnings("unused")
  private void setCourseComponents(List<CourseComponent> courseComponents) {
    this.courseComponents = courseComponents;
  }
  
  public void addCourseComponent(CourseComponent courseComponent) {
    if (courseComponent.getCourse() != null)
      courseComponent.getCourse().getCourseComponents().remove(courseComponent);
    courseComponent.setCourse(this);
    courseComponents.add(courseComponent);
  }
  
  public void removeCourseComponent(CourseComponent courseComponent) {
    courseComponent.setCourse(null);
    this.courseComponents.remove(courseComponent);
  } 

  public void setStudentCourseResources(List<StudentCourseResource> studentCourseResources) {
    this.studentCourseResources = studentCourseResources;
  }

  public List<StudentCourseResource> getStudentCourseResources() {
    return studentCourseResources;
  }

  public void setBasicCourseResources(List<BasicCourseResource> basicCourseResources) {
    this.basicCourseResources = basicCourseResources;
  }

  public List<BasicCourseResource> getBasicCourseResources() {
    return basicCourseResources;
  }

  public void setGradeCourseResources(List<GradeCourseResource> gradeCourseResources) {
    this.gradeCourseResources = gradeCourseResources;
  }

  public List<GradeCourseResource> getGradeCourseResources() {
    return gradeCourseResources;
  }

  public void setOtherCosts(List<OtherCost> otherCosts) {
    this.otherCosts = otherCosts;
  }

  public List<OtherCost> getOtherCosts() {
    return otherCosts;
  }
  
  public void addBasicCourseResource(BasicCourseResource basicCourseResource) {
    if (basicCourseResource.getCourse() != null)
      basicCourseResource.getCourse().getBasicCourseResources().remove(basicCourseResource);
    basicCourseResource.setCourse(this);
    basicCourseResources.add(basicCourseResource);
  }

  public void removeBasicCourseResource(BasicCourseResource basicCourseResource) {
    basicCourseResource.setCourse(null);
    basicCourseResources.remove(basicCourseResource);
  } 

  public void addStudentCourseResource(StudentCourseResource studentCourseResource) {
    if (studentCourseResource.getCourse() != null)
      studentCourseResource.getCourse().getStudentCourseResources().remove(studentCourseResource);
    studentCourseResource.setCourse(this);
    studentCourseResources.add(studentCourseResource);
  }

  public void removeStudentCourseResource(StudentCourseResource studentCourseResource) {
    studentCourseResource.setCourse(null);
    studentCourseResources.remove(studentCourseResource);
  } 

  public void addGradeCourseResource(GradeCourseResource gradeCourseResource) {
    if (gradeCourseResource.getCourse() != null)
      gradeCourseResource.getCourse().getGradeCourseResources().remove(gradeCourseResource);
    gradeCourseResource.setCourse(this);
    gradeCourseResources.add(gradeCourseResource);
  }

  public void removeGradeCourseResource(GradeCourseResource gradeCourseResource) {
    gradeCourseResource.setCourse(null);
    gradeCourseResources.remove(gradeCourseResource);
  } 

  public void addOtherCost(OtherCost otherCost) {
    if (otherCost.getCourse() != null)
      otherCost.getCourse().getOtherCosts().remove(otherCost);
    otherCost.setCourse(this);
    otherCosts.add(otherCost);
  }

  public void removeOtherCost(OtherCost otherCost) {
    otherCost.setCourse(null);
    otherCosts.remove(otherCost);
  } 

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getEndDate() {
    return endDate;
  }
  
  public String getNameExtension() {
  	return nameExtension;
  }
  
  public void setNameExtension(String nameExtension) {
	  this.nameExtension = nameExtension;
  }

  public void setLocalTeachingDays(Double localTeachingDays) {
    this.localTeachingDays = localTeachingDays;
  }

  public Double getLocalTeachingDays() {
    return localTeachingDays;
  }

  public void setState(CourseState state) {
    this.state = state;
  }

  public CourseState getState() {
    return state;
  }
  
  public CourseType getType() {
    return type;
  }
  
  public void setType(CourseType type) {
    this.type = type;
  }

  public void setTeachingHours(Double teachingHours) {
    this.teachingHours = teachingHours;
  }

  public Double getTeachingHours() {
    return teachingHours;
  }

  public void setDistanceTeachingDays(Double distanceTeachingDays) {
    this.distanceTeachingDays = distanceTeachingDays;
  }

  public Double getDistanceTeachingDays() {
    return distanceTeachingDays;
  }
  
  public Double getAssessingHours() {
    return assessingHours;
  }
  
  public void setAssessingHours(Double assessingHours) {
    this.assessingHours = assessingHours;
  }
  
  public Double getPlanningHours() {
    return planningHours;
  }
  
  public void setPlanningHours(Double planningHours) {
    this.planningHours = planningHours;
  }

  public Set<Tag> getTags() {
    return tags;
  }
  
  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
  
  public void addTag(Tag tag) {
    if (!tags.contains(tag)) {
      tags.add(tag);
    } else {
      throw new PersistenceException("Entity already has this tag");
    }
  }
  
  public void removeTag(Tag tag) {
    if (tags.contains(tag)) {
      tags.remove(tag);
    } else {
      throw new PersistenceException("Entity does not have this tag");
    }
  }
  
  public void setEnrolmentTimeEnd(Date enrolmentTimeEnd) {
    this.enrolmentTimeEnd = enrolmentTimeEnd;
  }

  public Date getEnrolmentTimeEnd() {
    return enrolmentTimeEnd;
  }

  public Double getDistanceTeachingHours() {
    return distanceTeachingHours;
  }

  public void setDistanceTeachingHours(Double distanceTeachingHours) {
    this.distanceTeachingHours = distanceTeachingHours;
  }

  @ManyToOne
  @JoinColumn(name="module")
  private Module module;
  
  @ManyToOne
  @JoinColumn(name="state")
  @IndexedEmbedded
  private CourseState state;
  
  @ManyToOne
  private CourseType type;
  
  @Field
  private String nameExtension;
  
  @Temporal (value=TemporalType.DATE)
  @Field (analyze = Analyze.NO)
  @DateBridge (resolution = Resolution.DAY)
  private Date beginDate;
  
  @Temporal (value=TemporalType.DATE)
  @Field (analyze = Analyze.NO)
  @DateBridge (resolution = Resolution.DAY)
  private Date endDate;
  
  private Double distanceTeachingDays;
  
  private Double distanceTeachingHours;

  private Double planningHours; 
  
  private Double localTeachingDays;
  
  private Double teachingHours;
  
  private Double assessingHours;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @IndexColumn (name = "indexColumn")
  @JoinColumn (name="course")
  @IndexedEmbedded
  private List<CourseComponent> courseComponents = new Vector<>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="course")
  private List<BasicCourseResource> basicCourseResources = new Vector<>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="course")
  private List<StudentCourseResource> studentCourseResources = new Vector<>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="course")
  private List<GradeCourseResource> gradeCourseResources = new Vector<>();

  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn (name="course")
  private List<OtherCost> otherCosts = new Vector<>();

  @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable (name="__CourseTags", joinColumns=@JoinColumn(name="course"), inverseJoinColumns=@JoinColumn(name="tag"))
  @IndexedEmbedded 
  private Set<Tag> tags = new HashSet<>();
  
  @Column
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date enrolmentTimeEnd;
}
