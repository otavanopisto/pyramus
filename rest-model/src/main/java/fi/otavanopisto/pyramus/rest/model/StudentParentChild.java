package fi.otavanopisto.pyramus.rest.model;

import java.time.LocalDate;

import fi.otavanopisto.security.ContextReference;

public class StudentParentChild implements ContextReference {

  public StudentParentChild() {
    super();
  }
  
  public StudentParentChild(Long studentId, Long personId, String firstName, String lastName, String nickname,
      String studyProgrammeName, String defaultEmail, String defaultPhoneNumber, Address defaultAddress,
      LocalDate studyStartDate, LocalDate studyTimeEnd, LocalDate studyEndDate) {
    super();
    this.studentId = studentId;
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.nickname = nickname;
    this.studyProgrammeName = studyProgrammeName;
    this.defaultEmail = defaultEmail;
    this.defaultPhoneNumber = defaultPhoneNumber;
    this.defaultAddress = defaultAddress;
    this.studyStartDate = studyStartDate;
    this.studyTimeEnd = studyTimeEnd;
    this.studyEndDate = studyEndDate;
  }

  public Long getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }
  
  public Long getPersonId() {
    return personId;
  }
  
  public void setPersonId(Long personId) {
    this.personId = personId;
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
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
  
  public String getStudyProgrammeName() {
    return studyProgrammeName;
  }
  
  public void setStudyProgrammeName(String studyProgrammeName) {
    this.studyProgrammeName = studyProgrammeName;
  }
  
  public String getDefaultEmail() {
    return defaultEmail;
  }
  
  public void setDefaultEmail(String defaultEmail) {
    this.defaultEmail = defaultEmail;
  }
  
  public String getDefaultPhoneNumber() {
    return defaultPhoneNumber;
  }
  
  public void setDefaultPhoneNumber(String defaultPhoneNumber) {
    this.defaultPhoneNumber = defaultPhoneNumber;
  }
  
  public Address getDefaultAddress() {
    return defaultAddress;
  }

  public void setDefaultAddress(Address defaultAddress) {
    this.defaultAddress = defaultAddress;
  }

  public LocalDate getStudyStartDate() {
    return studyStartDate;
  }

  public void setStudyStartDate(LocalDate studyStartDate) {
    this.studyStartDate = studyStartDate;
  }

  public LocalDate getStudyTimeEnd() {
    return studyTimeEnd;
  }

  public void setStudyTimeEnd(LocalDate studyTimeEnd) {
    this.studyTimeEnd = studyTimeEnd;
  }

  public LocalDate getStudyEndDate() {
    return studyEndDate;
  }

  public void setStudyEndDate(LocalDate studyEndDate) {
    this.studyEndDate = studyEndDate;
  }

  private Long studentId;
  private Long personId;
  private String firstName;
  private String lastName;
  private String nickname;
  private String studyProgrammeName;
  private String defaultEmail;
  private String defaultPhoneNumber;
  private Address defaultAddress;
  private LocalDate studyStartDate;
  private LocalDate studyTimeEnd;
  private LocalDate studyEndDate;
}
