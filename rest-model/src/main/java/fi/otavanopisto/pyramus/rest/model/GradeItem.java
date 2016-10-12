package fi.otavanopisto.pyramus.rest.model;

public class GradeItem {
  
  public GradeItem() {
  }
  
  public GradeItem(Long scaleId, String scaleName, Long gradeId, String gradeName) {
    this.scaleId = scaleId;
    this.scaleName = scaleName;
    this.gradeId = gradeId;
    this.gradeName = gradeName;
  }

  public Long getScaleId() {
    return scaleId;
  }

  public void setScaleId(Long scaleId) {
    this.scaleId = scaleId;
  }

  public String getScaleName() {
    return scaleName;
  }

  public void setScaleName(String scaleName) {
    this.scaleName = scaleName;
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

  private Long scaleId;
  private String scaleName;
  private Long gradeId;
  private String gradeName;

}
