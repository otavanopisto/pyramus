package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

public class StudentAPI {

  public StudentAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long createAbstractStudent(Date birthday, String socialSecurityNumber, String sex, String basicInfo, boolean secureInfo) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    Sex sexEntity;

    if ("m".equals(sex)) {
      sexEntity = Sex.MALE;
    } else if ("f".equals(sex)) {
      sexEntity = Sex.FEMALE;
    } else {
      throw new IllegalArgumentException("sex must be \"m\" or \"f\"");
    }

    AbstractStudent abstractStudent = abstractStudentDAO.create(birthday, socialSecurityNumber, sexEntity, basicInfo, secureInfo);

    return (abstractStudent.getId());
  }

  public Long createActivityType(String name) {
    return (DAOFactory.getInstance().getStudentActivityTypeDAO().create(name).getId());
  }

  public Long createExaminationType(String name) {
    return (DAOFactory.getInstance().getStudentExaminationTypeDAO().create(name).getId());
  }

  public Long createEducationalLevel(String name) {
    return (DAOFactory.getInstance().getStudentEducationalLevelDAO().create(name).getId());
  }

  public Long createNationality(String name, String code) {
    return (DAOFactory.getInstance().getNationalityDAO().create(name, code).getId());
  }

  public Long createLanguage(String name, String code) {
    return (DAOFactory.getInstance().getLanguageDAO().create(name, code).getId());
  }

  public Long createMunicipality(String name, String code) {
    return (DAOFactory.getInstance().getMunicipalityDAO().create(name, code).getId());
  }

  public Long createSchoolField(String name) {
    return (DAOFactory.getInstance().getSchoolFieldDAO().create(name).getId());
  }

  public Long createSchool(String code, String name, Long schoolField) {
    SchoolField schoolFieldEntity = null;
    if (schoolField != null) {
      schoolFieldEntity = DAOFactory.getInstance().getSchoolFieldDAO().findById(schoolField);
    }
    return (DAOFactory.getInstance().getSchoolDAO().create(code, name, schoolFieldEntity).getId());
  }

  public Long createStudent(Long abstractStudent, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      Long activityType, Long examinationType, Long educationalLevel, String education, Long nationality, Long municipality, Long language, Long school,
      Long studyProgramme, double previousStudies, Date studyStartDate, Date studyEndDate, Long studyEndReason, String studyEndText, boolean lodging) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    AbstractStudent abstractStudentEntity = null;
    if (abstractStudent != null) {
      abstractStudentEntity = DAOFactory.getInstance().getAbstractStudentDAO().findById(abstractStudent);
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
    School schoolEntity = null;
    if (school != null) {
      DAOFactory.getInstance().getSchoolDAO().findById(school);
    }
    StudyProgramme studyProgrammeEntity = null;
    if (studyProgramme != null) {
      DAOFactory.getInstance().getStudyProgrammeDAO().findById(studyProgramme);
    }
    StudentStudyEndReason studyEndReasonEntity = null;
    if (studyEndReason != null) {
      DAOFactory.getInstance().getStudentStudyEndReasonDAO().findById(studyEndReason);
    }

    Student student = studentDAO.create(abstractStudentEntity, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityTypeEntity,
        examinationTypeEntity, educationalLevelEntity, education, nationalityEntity, municipalityEntity, languageEntity, schoolEntity, studyProgrammeEntity,
        previousStudies, studyStartDate, studyEndDate, studyEndReasonEntity, studyEndText, lodging);
    return (student.getId());
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
