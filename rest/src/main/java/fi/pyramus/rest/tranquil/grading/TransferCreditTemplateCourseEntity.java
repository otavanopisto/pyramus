package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.TransferCreditTemplateCourse.class, entityType = TranquilModelType.COMPACT)
public class TransferCreditTemplateCourseEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public fi.pyramus.domainmodel.base.CourseOptionality getOptionality() {
    return optionality;
  }

  public void setOptionality(fi.pyramus.domainmodel.base.CourseOptionality optionality) {
    this.optionality = optionality;
  }

  public Long getTransferCreditTemplate_id() {
    return transferCreditTemplate_id;
  }

  public void setTransferCreditTemplate_id(Long transferCreditTemplate_id) {
    this.transferCreditTemplate_id = transferCreditTemplate_id;
  }

  public Long getCourseLength_id() {
    return courseLength_id;
  }

  public void setCourseLength_id(Long courseLength_id) {
    this.courseLength_id = courseLength_id;
  }

  public Long getSubject_id() {
    return subject_id;
  }

  public void setSubject_id(Long subject_id) {
    this.subject_id = subject_id;
  }

  private Long id;

  private Long version;

  private String courseName;

  private Integer courseNumber;

  private fi.pyramus.domainmodel.base.CourseOptionality optionality;

  private Long transferCreditTemplate_id;

  private Long courseLength_id;

  private Long subject_id;

  public final static String[] properties = {"id","version","courseName","courseNumber","optionality","transferCreditTemplate","courseLength","subject"};
}
