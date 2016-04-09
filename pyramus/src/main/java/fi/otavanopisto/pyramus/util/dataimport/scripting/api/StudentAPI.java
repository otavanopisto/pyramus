package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.Language;
import fi.otavanopisto.pyramus.domainmodel.base.Municipality;
import fi.otavanopisto.pyramus.domainmodel.base.Nationality;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudentAPI {

  public StudentAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(Long personId, String firstName, String lastName, String email, Long emailContactTypeId, String nickname, String additionalInfo, Date studyTimeEnd, Long activityType,
      Long examinationType, Long educationalLevel, String education, Long nationality, Long municipality, Long language, Long schoolId, Long studyProgrammeId,
      Double previousStudies, Date studyStartDate, Date studyEndDate, Long studyEndReasonId, String studyEndText, boolean lodging) throws InvalidScriptException {

    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    email = email != null ? email.trim() : null;
    
    Person personEntity = null;
    if (personId != null) {
      personEntity = personDAO.findById(personId);
    }
    StudentActivityType activityTypeEntity = null;
    if (activityType != null) {
      activityTypeEntity = DAOFactory.getInstance().getStudentActivityTypeDAO().findById(activityType);
    }
    StudentExaminationType examinationTypeEntity = null;
    if (examinationType != null) {
      examinationTypeEntity = DAOFactory.getInstance().getStudentExaminationTypeDAO().findById(examinationType);
    }
    StudentEducationalLevel educationalLevelEntity = null;
    if (educationalLevel != null) {
      educationalLevelEntity = DAOFactory.getInstance().getStudentEducationalLevelDAO().findById(educationalLevel);
    }
    
    Nationality nationalityEntity = null;
    if (nationality != null) {
      nationalityEntity = DAOFactory.getInstance().getNationalityDAO().findById(nationality);
    }
    
    Municipality municipalityEntity = null;
    if (municipality != null) {
      municipalityEntity = DAOFactory.getInstance().getMunicipalityDAO().findById(municipality);
    }
    
    Language languageEntity = null;
    if (language != null) {
      languageEntity = DAOFactory.getInstance().getLanguageDAO().findById(language);
    }
    
    School school = null;
    if (schoolId != null) {
      school = DAOFactory.getInstance().getSchoolDAO().findById(schoolId);
    }
    
    StudyProgramme studyProgramme = null;
    if (studyProgrammeId != null) {
      studyProgramme = DAOFactory.getInstance().getStudyProgrammeDAO().findById(studyProgrammeId);
    }
    
    StudentStudyEndReason studyEndReason = null;
    if (studyEndReasonId != null) {
      studyEndReason = DAOFactory.getInstance().getStudentStudyEndReasonDAO().findById(studyEndReasonId);
    }

    Student student = studentDAO.create(personEntity, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityTypeEntity,
        examinationTypeEntity, educationalLevelEntity, education, nationalityEntity, municipalityEntity, languageEntity, school, studyProgramme,
        previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText, lodging, false);
    
    // Default user
    
    if (personEntity.getDefaultUser() == null) {
      personDAO.updateDefaultUser(personEntity, student);
    }
    
    if (StringUtils.isNotBlank(email)) {
      ContactType emailContactType = contactTypeDAO.findById(emailContactTypeId);
      if (emailContactType == null) {
        throw new InvalidScriptException("Could not find contact type for email");
      }
      
      emailDAO.create(student.getContactInfo(), emailContactType, true, email);
    }
    
    return student.getId();
  }

  public void updateStudentPerson(Long studentId, Long personId) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    
    Student student = studentDAO.findById(studentId);
    Person person = personDAO.findById(personId);
    
    studentDAO.updatePerson(student, person);
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
