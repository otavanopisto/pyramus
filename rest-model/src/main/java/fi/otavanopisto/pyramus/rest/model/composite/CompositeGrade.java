package fi.otavanopisto.pyramus.rest.model.composite;

public class CompositeGrade {
  
  public CompositeGrade() {
  }
  
  public CompositeGrade(Long gradeId, String gradeName) {
    this.gradeId = gradeId;
    this.gradeName = gradeName;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  private Long gradeId;
  private String gradeName;

}
