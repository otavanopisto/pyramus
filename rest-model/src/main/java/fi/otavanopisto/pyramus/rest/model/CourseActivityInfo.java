package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

public class CourseActivityInfo {

  public String getLineName() {
    return lineName;
  }

  public void setLineName(String lineName) {
    this.lineName = lineName;
  }

  public String getLineCategory() {
    return lineCategory;
  }

  public void setLineCategory(String lineCategory) {
    this.lineCategory = lineCategory;
  }

  public boolean isDefaultLine() {
    return isDefaultLine;
  }

  public void setDefaultLine(boolean isDefaultLine) {
    this.isDefaultLine = isDefaultLine;
  }

  public List<CourseActivity> getActivities() {
    return activities;
  }

  public void setActivities(List<CourseActivity> activities) {
    this.activities = activities;
  }

  private String lineName;
  private String lineCategory;
  private boolean isDefaultLine;
  private List<CourseActivity> activities;

}
