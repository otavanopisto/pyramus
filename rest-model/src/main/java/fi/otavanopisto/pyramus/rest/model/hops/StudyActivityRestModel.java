package fi.otavanopisto.pyramus.rest.model.hops;

import java.util.List;

public class StudyActivityRestModel {
  
  public List<StudyActivityItemRestModel> getItems() {
    return items;
  }

  public void setItems(List<StudyActivityItemRestModel> items) {
    this.items = items;
  }

  public String getEducationType() {
    return educationType;
  }

  public void setEducationType(String educationType) {
    this.educationType = educationType;
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

  private String educationType;
  private List<StudyActivityItemRestModel> items;
  private int completedCourseCredits;
  private int mandatoryCourseCredits;

}
