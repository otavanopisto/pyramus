package fi.otavanopisto.pyramus.services.entities.grading;

public class GradeEntity {
  
  public GradeEntity() {
  }

  public GradeEntity(Long id, String name, String description, GradingScaleEntity gradingScale, Boolean passingGrade, Boolean archived, String qualification,
      Double gpa) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.gradingScale = gradingScale;
    this.passingGrade = passingGrade;
    this.archived = archived;
    this.qualification = qualification;
    this.GPA = gpa;
  }

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

  public GradingScaleEntity getGradingScale() {
    return gradingScale;
  }

  public void setGradingScale(GradingScaleEntity gradingScale) {
    this.gradingScale = gradingScale;
  }

  public Boolean getPassingGrade() {
    return passingGrade;
  }

  public void setPassingGrade(Boolean passingGrade) {
    this.passingGrade = passingGrade;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public Double getGPA() {
    return GPA;
  }

  public void setGPA(Double gPA) {
    GPA = gPA;
  }

  private Long id;
  private String name;
  private String description;
  private GradingScaleEntity gradingScale;
  private Boolean passingGrade;
  private Boolean archived;
  private String qualification;
  private Double GPA;
}
