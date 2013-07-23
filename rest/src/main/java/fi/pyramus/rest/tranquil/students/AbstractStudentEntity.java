package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel  (entityClass = fi.pyramus.domainmodel.students.AbstractStudent.class, entityType = TranquilModelType.COMPACT)
public class AbstractStudentEntity implements fi.tranquil.TranquilModelEntity {

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.util.Date getBirthday() {
    return birthday;
  }

  public void setBirthday(java.util.Date birthday) {
    this.birthday = birthday;
  }

  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public fi.pyramus.domainmodel.students.Sex getSex() {
    return sex;
  }

  public void setSex(fi.pyramus.domainmodel.students.Sex sex) {
    this.sex = sex;
  }

  public String getBasicInfo() {
    return basicInfo;
  }

  public void setBasicInfo(String basicInfo) {
    this.basicInfo = basicInfo;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Boolean getSecureInfo() {
    return secureInfo;
  }

  public void setSecureInfo(Boolean secureInfo) {
    this.secureInfo = secureInfo;
  }

  public java.util.List<Long> getStudents_ids() {
    return students_ids;
  }

  public void setStudents_ids(java.util.List<Long> students_ids) {
    this.students_ids = students_ids;
  }

  private Long id;

  private java.util.Date birthday;

  private String socialSecurityNumber;

  private fi.pyramus.domainmodel.students.Sex sex;

  private String basicInfo;

  private Long version;

  private Boolean secureInfo;

  private java.util.List<Long> students_ids;

  public final static String[] properties = {"id","birthday","socialSecurityNumber","sex","basicInfo","version","secureInfo","students"};
}
