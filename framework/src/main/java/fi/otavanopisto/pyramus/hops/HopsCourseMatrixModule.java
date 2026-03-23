package fi.otavanopisto.pyramus.hops;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HopsCourseMatrixModule {

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

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public boolean isHiddenFromHops() {
    return hiddenFromHops;
  }

  public void setHiddenFromHops(boolean hiddenFromHops) {
    this.hiddenFromHops = hiddenFromHops;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public Mandatority getMandatority() {
    return mandatority;
  }

  public void setMandatority(Mandatority mandatority) {
    this.mandatority = mandatority;
  }

  private String name;
  private int courseNumber;
  private int length;
  private Mandatority mandatority;
  private boolean hiddenFromHops;
  private boolean available;

}
