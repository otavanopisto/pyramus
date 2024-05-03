package fi.otavanopisto.pyramus.tor.curriculum;

public class TORCurriculumModule {

  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public int getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(int courseNumber) {
    this.courseNumber = courseNumber;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  private String name;
  private int courseNumber;
  private int length;
  private boolean mandatory;
}
