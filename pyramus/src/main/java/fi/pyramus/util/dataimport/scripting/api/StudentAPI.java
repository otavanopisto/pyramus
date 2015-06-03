package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.ContactTypeDAO;
import fi.pyramus.dao.base.EmailDAO;
import fi.pyramus.dao.base.PersonDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.Person;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

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
    
    return (student.getId());
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
