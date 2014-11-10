package fi.pyramus.services.entities.base;

import java.util.Date;

public class PersonEntity {

  public PersonEntity() {
  }
  
  public PersonEntity(Long id, Date birthday, String socialSecurityNumber, String sex) {
    this.id = id;
    this.birthday = birthday;
    this.socialSecurityNumber = socialSecurityNumber;
    this.sex = sex;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  private Long id;
  private Date birthday;
  private String socialSecurityNumber;
  private String sex;
}
