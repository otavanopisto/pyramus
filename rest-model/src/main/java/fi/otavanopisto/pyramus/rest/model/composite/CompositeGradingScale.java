package fi.otavanopisto.pyramus.rest.model.composite;

import java.util.List;

public class CompositeGradingScale {
  
  public CompositeGradingScale() {
  }
  
  public CompositeGradingScale(Long scaleId, String scaleName, List<CompositeGrade> grades) {
    this.scaleId = scaleId;
    this.scaleName = scaleName;
    this.grades = grades;
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

  public List<CompositeGrade> getGrades() {
    return grades;
  }

  public void setScaleName(String scaleName) {
    this.scaleName = scaleName;
  }
  
  private Long scaleId;
  private String scaleName;
  private List<CompositeGrade> grades;

}
