package fi.pyramus.rest.tranquil.grading;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.grading.Grade.class, entityType = TranquilModelType.COMPACT)
public class GradeEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public Double getGPA() {
    return gPA;
  }

  public void setGPA(Double gPA) {
    this.gPA = gPA;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getGradingScale_id() {
    return gradingScale_id;
  }

  public void setGradingScale_id(Long gradingScale_id) {
    this.gradingScale_id = gradingScale_id;
  }

  private Long id;

  private String name;

  private String description;

  private Boolean passingGrade;

  private String qualification;

  private Double gPA;

  private Boolean archived;

  private Long version;

  private Long gradingScale_id;

  public final static String[] properties = {"id","name","description","passingGrade","qualification","gPA","archived","version","gradingScale"};
}
