package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

public class Person {

  public Person() {
    super();
  }

  public Person(Long id, OffsetDateTime birthday, String socialSecurityNumber, Sex sex, Boolean secureInfo, String basicInfo, Long defaultUserId) {
    super();
    this.id = id;
    this.birthday = birthday;
    this.socialSecurityNumber = socialSecurityNumber;
    this.sex = sex;
    this.secureInfo = secureInfo;
    this.basicInfo = basicInfo;
    this.defaultUserId = defaultUserId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OffsetDateTime getBirthday() {
    return birthday;
  }

  public void setBirthday(OffsetDateTime birthday) {
    this.birthday = birthday;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }
  
  public Long getDefaultUserId() {
    return defaultUserId;
  }
  
  public void setDefaultUserId(Long defaultUserId) {
    this.defaultUserId = defaultUserId;
  }

  private Long id;
  private OffsetDateTime birthday;
  private String socialSecurityNumber;
  private Sex sex;
  private Boolean secureInfo;
  private String basicInfo;
  private Long defaultUserId;
}
