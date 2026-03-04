package fi.otavanopisto.pyramus.rest.model.hops;

import java.util.List;

public class StudyActivityRestModel {
  
  public List<StudyActivityItemRestModel> getItems() {
    return items;
  }

  public void setItems(List<StudyActivityItemRestModel> items) {
    this.items = items;
  }

  public String getEducationTypeCode() {
    return educationTypeCode;
  }

  public void setEducationTypeCode(String educationTypeCode) {
    this.educationTypeCode = educationTypeCode;
  }

  public int getCompletedCourseCredits() {
    return completedCourseCredits;
  }

  public void setCompletedCourseCredits(int completedCourseCredits) {
    this.completedCourseCredits = completedCourseCredits;
  }

  public int getMandatoryCourseCredits() {
    return mandatoryCourseCredits;
  }

  public void setMandatoryCourseCredits(int mandatoryCourseCredits) {
    this.mandatoryCourseCredits = mandatoryCourseCredits;
  }

  private String educationTypeCode;
  private List<StudyActivityItemRestModel> items;
  private int completedCourseCredits;
  private int mandatoryCourseCredits;

}
