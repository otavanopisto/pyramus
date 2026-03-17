package fi.otavanopisto.pyramus.tor.curriculum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fi.otavanopisto.pyramus.hops.Mandatority;

@JsonIgnoreProperties(ignoreUnknown = true)
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

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public String getLengthUnitSymbol() {
    return lengthUnitSymbol;
  }

  public void setLengthUnitSymbol(String lengthUnitSymbol) {
    this.lengthUnitSymbol = lengthUnitSymbol;
  }

  public Mandatority getMandatority() {
    return mandatority;
  }

  public void setMandatority(Mandatority mandatority) {
    this.mandatority = mandatority;
  }
  
  @JsonIgnore
  public boolean isMandatory() {
    return mandatority != null && mandatority == Mandatority.MANDATORY;
  }
  

  private String name;
  private int courseNumber;
  private int length;
  private String lengthUnitSymbol;
  private Mandatority mandatority;
}
