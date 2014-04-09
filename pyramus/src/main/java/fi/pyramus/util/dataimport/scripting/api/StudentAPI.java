package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

public class StudentAPI {
  
  public Handle createAbstractStudent(Date birthday,
                                      String socialSecurityNumber,
                                      String sex,
                                      String basicInfo,
                                      boolean secureInfo
                                      ) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    Sex sexEntity;
    
    if ("m".equals(sex)) {
      sexEntity = Sex.MALE;
    } else if ("f".equals(sex)) {
      sexEntity = Sex.FEMALE;
    } else {
      throw new IllegalArgumentException("sex must be \"m\" or \"f\"");
    }
    
    AbstractStudent abstractStudent = 
        abstractStudentDAO.create(birthday, socialSecurityNumber, sexEntity, basicInfo, secureInfo);
    
    return new Handle(abstractStudent.getId());
  }
  
  public Handle createActivityType(String name) {
    return new Handle(DAOFactory.getInstance().getStudentActivityTypeDAO().create(name).getId());
  }
  
  public Handle createExaminationType(String name) {
    return new Handle(DAOFactory.getInstance().getStudentExaminationTypeDAO().create(name).getId());
  }
  
  public Handle createEducationalLevel(String name) {
    return new Handle(DAOFactory.getInstance().getStudentEducationalLevelDAO().create(name).getId());
  }
  
  public Handle createNationality(String name, String code) {
    return new Handle(DAOFactory.getInstance().getNationalityDAO().create(name, code).getId());
  }
  
  public Handle createMunicipality(String name, String code) {
    return new Handle(DAOFactory.getInstance().getMunicipalityDAO().create(name, code).getId());
  }
  
  public Handle createSchoolField(String name) {
    return new Handle(DAOFactory.getInstance().getSchoolFieldDAO().create(name).getId());
  }
  
  public Handle createSchool(String code, String name, Handle schoolField) {
    SchoolField schoolFieldEntity = DAOFactory.getInstance().getSchoolFieldDAO().findById(schoolField.getId());
    return new Handle(DAOFactory.getInstance().getSchoolDAO().create(code, name, schoolFieldEntity).getId());
  }
  
  public Handle createEducationType(String name, String code) {
    return new Handle(DAOFactory.getInstance().getEducationTypeDAO().create(name, code).getId());
  }
  
  public Handle createStudyProgrammeCategory(String name, Handle educationType) {
    EducationType educationTypeEntity = DAOFactory.getInstance().getEducationTypeDAO().findById(educationType.getId());
    return new Handle(DAOFactory.getInstance().getStudyProgrammeCategoryDAO().create(name, educationTypeEntity).getId());
  }
  
  public Handle createStudyProgramme(String name, Handle category, String code) {
    StudyProgrammeCategory categoryEntity = DAOFactory.getInstance().getStudyProgrammeCategoryDAO().findById(category.getId());
    return new Handle(DAOFactory.getInstance().getStudyProgrammeDAO().create(name, categoryEntity, code).getId());
  }
  
  public Handle createStudent(Handle abstractStudent,
                              String firstName,
                              String lastName,
                              String nickname,
                              String additionalInfo,
                              Date studyTimeEnd,
                              Handle activityType,
                              Handle examinationType,
                              Handle educationalLevel,
                              String education,
                              Handle nationality,
                              Handle municipality,
                              Handle language,
                              Handle school,
                              Handle studyProgramme,
                              double previousStudies,
                              Date studyStartDate,
                              Date studyEndDate,
                              Handle studyEndReason,
                              String studyEndText,
                              boolean lodging) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    
    AbstractStudent abstractStudentEntity =
        DAOFactory.getInstance().getAbstractStudentDAO().findById(abstractStudent.getId());
    StudentActivityType activityTypeEntity =
        DAOFactory.getInstance().getStudentActivityTypeDAO().findById(activityType.getId());
    StudentExaminationType examinationTypeEntity =
        DAOFactory.getInstance().getStudentExaminationTypeDAO().findById(examinationType.getId());
    StudentEducationalLevel educationalLevelEntity =
        DAOFactory.getInstance().getStudentEducationalLevelDAO().findById(educationalLevel.getId());
    Nationality nationalityEntity =
        DAOFactory.getInstance().getNationalityDAO().findById(nationality.getId());
    Municipality municipalityEntity =
        DAOFactory.getInstance().getMunicipalityDAO().findById(municipality.getId());
    Language languageEntity =
        DAOFactory.getInstance().getLanguageDAO().findById(language.getId());
    School schoolEntity =
        DAOFactory.getInstance().getSchoolDAO().findById(school.getId());
    StudyProgramme studyProgrammeEntity =
        DAOFactory.getInstance().getStudyProgrammeDAO().findById(studyProgramme.getId());
    StudentStudyEndReason studyEndReasonEntity =
        DAOFactory.getInstance().getStudentStudyEndReasonDAO().findById(studyEndReason.getId());
    
    Student student = studentDAO.create(abstractStudentEntity,
                                        firstName,
                                        lastName,
                                        nickname,
                                        additionalInfo,
                                        studyTimeEnd,
                                        activityTypeEntity,
                                        examinationTypeEntity,
                                        educationalLevelEntity,
                                        education,
                                        nationalityEntity,
                                        municipalityEntity,
                                        languageEntity,
                                        schoolEntity,
                                        studyProgrammeEntity,
                                        previousStudies,
                                        studyStartDate,
                                        studyEndDate,
                                        studyEndReasonEntity,
                                        studyEndText,
                                        lodging);
    return new Handle(student.getId());
  }

}
