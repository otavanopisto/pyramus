package fi.otavanopisto.pyramus.domainmodel.courses;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.security.ContextReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Indexed
@PrimaryKeyJoinColumn(name="id")
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
  
  public void setSignupStart(Date signupStart) {
    this.signupStart = signupStart;
  }

  public Date getSignupStart() {
    return signupStart;
  }
  
  public void setSignupEnd(Date signupEnd) {
    this.signupEnd = signupEnd;
  }

  public Date getSignupEnd() {
    return signupEnd;
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
  
  public void setOrganization(Organization organization) {
    this.organization = organization;
  }
  
  public Organization getOrganization() {
    return organization;
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
  
  public BigDecimal getCourseFee() {
    return courseFee;
  }
  
  public void setCourseFee(BigDecimal courseFee) {
    this.courseFee = courseFee;
  }
  
  public Currency getCourseFeeCurrency() {
    return courseFeeCurrency;
  }
  
  public void setCourseFeeCurrency(Currency courseFeeCurrency) {
    this.courseFeeCurrency = courseFeeCurrency;
  }

  public boolean isCourseTemplate() {
    return courseTemplate;
  }

  public void setCourseTemplate(boolean courseTemplate) {
    this.courseTemplate = courseTemplate;
  }

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name="module")
  private Module module;
  
  @ManyToOne
  @JoinColumn(name="state")
  @IndexedEmbedded
  private CourseState state;
  
  @ManyToOne
  @JoinColumn(name="organization")
  private Organization organization;
  
  @ManyToOne
  private CourseType type;
  
  @FullTextField
  private String nameExtension;
  
  @Temporal (value=TemporalType.DATE)
  @GenericField
  private Date beginDate;
  
  @Temporal (value=TemporalType.DATE)
  @GenericField
  private Date endDate;
  
  @Temporal (value=TemporalType.DATE)
  @GenericField
  private Date signupStart;
  
  @Temporal (value=TemporalType.DATE)
  @GenericField
  private Date signupEnd;
  
  private Double distanceTeachingDays;
  
  private Double distanceTeachingHours;

  private Double planningHours; 
  
  private Double localTeachingDays;
  
  private Double teachingHours;
  
  private Double assessingHours;
  
  @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn (name = "indexColumn")
  @JoinColumn (name="course")
  @IndexedEmbedded
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
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
  @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
  private Set<Tag> tags = new HashSet<>();
  
  @Column
  @Temporal (value=TemporalType.TIMESTAMP)
  private Date enrolmentTimeEnd;
  
  private BigDecimal courseFee;
  
  private Currency courseFeeCurrency;

  @GenericField
  private boolean courseTemplate;
}
