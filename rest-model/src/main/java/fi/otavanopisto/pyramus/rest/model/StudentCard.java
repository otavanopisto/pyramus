package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;

public class StudentCard {
  
  public StudentCard(Long id, Long userEntityId, String firstName, String lastName, String studyProgramme,
      Date expiryDate, StudentCardActivity activity, StudentCardType type) {
    super();
    this.id = id;
    this.userEntityId = userEntityId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.studyProgramme = studyProgramme;
    this.expiryDate = expiryDate;
    this.activity = activity;
    this.type = type;
  }

  public StudentCard() {
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

  public String getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(String studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public Long getUserEntityId() {
    return userEntityId;
  }

  public void setUserEntityId(Long userEntityId) {
    this.userEntityId = userEntityId;
  }

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public StudentCardType getType() {
    return type;
  }

  public void setType(StudentCardType type) {
    this.type = type;
  }

  public StudentCardActivity getActivity() {
    return activity;
  }

  public void setActivity(StudentCardActivity activity) {
    this.activity = activity;
  }

  private Long id;
  private Long userEntityId;
  private String firstName;
  private String lastName;
  private String studyProgramme;
  private Date expiryDate;
  private StudentCardActivity activity;
  private StudentCardType type; 
}
