package fi.otavanopisto.pyramus.services.entities.students;

import java.util.Date;

import fi.otavanopisto.pyramus.services.entities.base.AddressEntity;
import fi.otavanopisto.pyramus.services.entities.base.LanguageEntity;
import fi.otavanopisto.pyramus.services.entities.base.MunicipalityEntity;
import fi.otavanopisto.pyramus.services.entities.base.NationalityEntity;
import fi.otavanopisto.pyramus.services.entities.base.SchoolEntity;
import fi.otavanopisto.pyramus.services.entities.base.StudyProgrammeEntity;

public class StudentEntity {

  public StudentEntity() {
  }

  public StudentEntity(Long id, AbstractStudentEntity abstractStudent, String[] emails, String firstName, String lastName, String[] tags,
      AddressEntity[] addresses, String phone, String additionalInfo, String parentalInfo, Date studyTimeEnd, NationalityEntity nationality,
      LanguageEntity language, MunicipalityEntity municipality, SchoolEntity school, StudyProgrammeEntity studyProgramme, Boolean archived,
      Date studyStartDate, Date studyEndDate) {
    super();
    this.id = id;
    this.abstractStudent = abstractStudent;
    this.emails = emails;
    this.firstName = firstName;
    this.lastName = lastName;
    this.tags = tags;
    this.addresses = addresses;
    this.phone = phone;
    this.additionalInfo = additionalInfo;
    this.studyTimeEnd = studyTimeEnd;
    this.parentalInfo = parentalInfo;
    this.nationality = nationality;
    this.language = language;
    this.municipality = municipality;
    this.school = school;
    this.archived = archived;
    this.studyProgramme = studyProgramme;
    this.studyStartDate = studyStartDate;
    this.studyEndDate = studyEndDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AbstractStudentEntity getAbstractStudent() {
    return abstractStudent;
  }
  
  public void setAbstractStudent(AbstractStudentEntity abstractStudent) {
    this.abstractStudent = abstractStudent;
  }

  public String[] getEmails() {
    return emails;
  }

  public void setEmails(String[] emails) {
    this.emails = emails;
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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getParentalInfo() {
    return parentalInfo;
  }

  public void setParentalInfo(String parentalInfo) {
    this.parentalInfo = parentalInfo;
  }

  public Date getStudyTimeEnd() {
    return studyTimeEnd;
  }

  public void setStudyTimeEnd(Date studyTimeEnd) {
    this.studyTimeEnd = studyTimeEnd;
  }

  public Date getStudyStartDate() {
    return studyStartDate;
  }

  public void setStudyStartDate(Date studyStartDate) {
    this.studyStartDate = studyStartDate;
  }

  public Date getStudyEndDate() {
    return studyEndDate;
  }

  public void setStudyEndDate(Date studyEndDate) {
    this.studyEndDate = studyEndDate;
  }

  public NationalityEntity getNationality() {
    return nationality;
  }

  public void setNationality(NationalityEntity nationality) {
    this.nationality = nationality;
  }

  public LanguageEntity getLanguage() {
    return language;
  }

  public void setLanguage(LanguageEntity language) {
    this.language = language;
  }

  public MunicipalityEntity getMunicipality() {
    return municipality;
  }

  public void setMunicipality(MunicipalityEntity municipality) {
    this.municipality = municipality;
  }

  public SchoolEntity getSchool() {
    return school;
  }

  public void setSchool(SchoolEntity school) {
    this.school = school;
  }

  public StudyProgrammeEntity getStudyProgramme() {
    return studyProgramme;
  }

  public void setStudyProgramme(StudyProgrammeEntity studyProgramme) {
    this.studyProgramme = studyProgramme;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public AddressEntity[] getAddresses() {
    return addresses;
  }

  public void setAddresses(AddressEntity[] addresses) {
    this.addresses = addresses;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  private Long id;
  private AbstractStudentEntity abstractStudent;
  private String[] emails;
  private String firstName;
  private String lastName;
  private String phone;
  private String additionalInfo;
  private String parentalInfo;
  private Date studyTimeEnd;
  private Date studyStartDate;
  private Date studyEndDate;
  private NationalityEntity nationality;
  private LanguageEntity language;
  private MunicipalityEntity municipality;
  private SchoolEntity school;
  private StudyProgrammeEntity studyProgramme;
  private Boolean archived;
  private AddressEntity[] addresses;
  private String[] tags;
}
