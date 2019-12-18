package fi.otavanopisto.pyramus.rest.model.muikku;

public class StudentPayload {

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getStudyProgrammeIdentifier() {
    return studyProgrammeIdentifier;
  }

  public void setStudyProgrammeIdentifier(String studyProgrammeIdentifier) {
    this.studyProgrammeIdentifier = studyProgrammeIdentifier;
  }

  private String identifier;
  private String firstName;
  private String lastName;
  private String email;
  private String studyProgrammeIdentifier;
  private String gender;
  private String ssn;

}
