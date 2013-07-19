package fi.pyramus.rest.tranquil.students;

import fi.tranquil.TranquilModel;
import fi.tranquil.TranquilModelType;

@TranquilModel (entityClass = fi.pyramus.domainmodel.students.Student.class, entityType = TranquilModelType.UPDATE)
public class StudentUpdate extends StudentComplete {

  public void setAbstractStudent(AbstractStudentCompact abstractStudent) {
    super.setAbstractStudent(abstractStudent);
  }

  public AbstractStudentCompact getAbstractStudent() {
    return (AbstractStudentCompact)super.getAbstractStudent();
  }

  public void setMunicipality(MunicipalityCompact municipality) {
    super.setMunicipality(municipality);
  }

  public MunicipalityCompact getMunicipality() {
    return (MunicipalityCompact)super.getMunicipality();
  }

  public void setNationality(NationalityCompact nationality) {
    super.setNationality(nationality);
  }

  public NationalityCompact getNationality() {
    return (NationalityCompact)super.getNationality();
  }

  public void setLanguage(LanguageCompact language) {
    super.setLanguage(language);
  }

  public LanguageCompact getLanguage() {
    return (LanguageCompact)super.getLanguage();
  }

  public void setSchool(SchoolCompact school) {
    super.setSchool(school);
  }

  public SchoolCompact getSchool() {
    return (SchoolCompact)super.getSchool();
  }

  public void setStudyProgramme(StudyProgrammeCompact studyProgramme) {
    super.setStudyProgramme(studyProgramme);
  }

  public StudyProgrammeCompact getStudyProgramme() {
    return (StudyProgrammeCompact)super.getStudyProgramme();
  }

  public void setStudyEndReason(StudentStudyEndReasonCompact studyEndReason) {
    super.setStudyEndReason(studyEndReason);
  }

  public StudentStudyEndReasonCompact getStudyEndReason() {
    return (StudentStudyEndReasonCompact)super.getStudyEndReason();
  }

  public void setActivityType(StudentActivityTypeCompact activityType) {
    super.setActivityType(activityType);
  }

  public StudentActivityTypeCompact getActivityType() {
    return (StudentActivityTypeCompact)super.getActivityType();
  }

  public void setEducationalLevel(StudentEducationalLevelCompact educationalLevel) {
    super.setEducationalLevel(educationalLevel);
  }

  public StudentEducationalLevelCompact getEducationalLevel() {
    return (StudentEducationalLevelCompact)super.getEducationalLevel();
  }

  public void setExaminationType(StudentExaminationTypeCompact examinationType) {
    super.setExaminationType(examinationType);
  }

  public StudentExaminationTypeCompact getExaminationType() {
    return (StudentExaminationTypeCompact)super.getExaminationType();
  }

  public void setContactInfo(ContactInfoCompact contactInfo) {
    super.setContactInfo(contactInfo);
  }

  public ContactInfoCompact getContactInfo() {
    return (ContactInfoCompact)super.getContactInfo();
  }

  public void setVariables(java.util.List<StudentVariableCompact> variables) {
    super.setVariables(variables);
  }

  public java.util.List<StudentVariableCompact> getVariables() {
    return (java.util.List<StudentVariableCompact>)super.getVariables();
  }

  public void setTags(java.util.List<TagCompact> tags) {
    super.setTags(tags);
  }

  public java.util.List<TagCompact> getTags() {
    return (java.util.List<TagCompact>)super.getTags();
  }

  public void setBillingDetails(java.util.List<BillingDetailsCompact> billingDetails) {
    super.setBillingDetails(billingDetails);
  }

  public java.util.List<BillingDetailsCompact> getBillingDetails() {
    return (java.util.List<BillingDetailsCompact>)super.getBillingDetails();
  }

  public final static String[] properties = {"abstractStudent","municipality","nationality","language","school","studyProgramme","studyEndReason","activityType","educationalLevel","examinationType","contactInfo","variables","tags","billingDetails"};
}
