package fi.otavanopisto.pyramus.tor;


public class Grade {
  
  public Grade() {
  }
  
  public Grade(Long id, String name, String description, Long gradingScaleId, Boolean passingGrade, String qualification, Double gpa, Boolean archived) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.gradingScaleId = gradingScaleId;
    this.passingGrade = passingGrade;
    this.archived = archived;
    this.qualification = qualification;
    this.gpa = gpa;
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
  
  public Long getGradingScaleId() {
    return gradingScaleId;
  }
  
  public void setGradingScaleId(Long gradingScaleId) {
    this.gradingScaleId = gradingScaleId;
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
  
  public Double getGpa() {
    return gpa;
  }
  
  public void setGpa(Double gpa) {
    this.gpa = gpa;
  }

  private Long id;
  private String name;
  private String description;
  private Long gradingScaleId;
  private Boolean passingGrade;
  private Boolean archived;
  private String qualification;
  private Double gpa;
}
