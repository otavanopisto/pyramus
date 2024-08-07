package fi.otavanopisto.pyramus.rest.model;

import java.util.Date;

public class SliceStudentCardRestModel {
  
  public SliceStudentCardRestModel(String firstName, String lastName, Date birthday, String email, 
     Date expiryDate, StudentCardType type) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.email = email;
    this.expiryDate = expiryDate;
    this.type = type;
  }

  public SliceStudentCardRestModel() {
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

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  private Long uid;
  private String firstName;
  private String lastName;
  private Date birthday;
  private String email;
  private Date expiryDate;
  private StudentCardType type;
}
