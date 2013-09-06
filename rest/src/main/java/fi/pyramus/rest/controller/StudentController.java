package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentActivityType;
import fi.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.pyramus.domainmodel.students.StudentExaminationType;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class StudentController {
  @Inject
  StudentDAO studentDAO;

  public Student createStudent(AbstractStudent abstractStudent, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {

    Student student = studentDAO.create(abstractStudent, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType,
        educationalLevel, education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate, studyEndDate,
        studyEndReason, studyEndText, lodging);

    return student;
  }

  public List<Student> findStudents() {
    List<Student> students = studentDAO.listAll();
    return students;
  }

  public List<Student> findUnarchivedStudents() {
    List<Student> students = studentDAO.listUnarchived();
    return students;
  }

  public Student findStudentById(Long id) {
    Student student = studentDAO.findById(id);
    return student;
  }
  
  public List<Student> findStudentByAbstractStudent(AbstractStudent abstractStudent) {
    List<Student> students = studentDAO.listByAbstractStudent(abstractStudent);
    return students;
  }
  
  public Student updateStudent(Student student, String firstName, String lastName, String nickname, String additionalInfo, Date studyTimeEnd,
      StudentActivityType activityType, StudentExaminationType examinationType, StudentEducationalLevel educationalLevel, String education,
      Nationality nationality, Municipality municipality, Language language, School school, StudyProgramme studyProgramme, Double previousStudies,
      Date studyStartDate, Date studyEndDate, StudentStudyEndReason studyEndReason, String studyEndText, Boolean lodging) {
    
    studentDAO.update(student, firstName, lastName, nickname, additionalInfo, studyTimeEnd, activityType, examinationType, educationalLevel,
        education, nationality, municipality, language, school, studyProgramme, previousStudies, studyStartDate, studyEndDate, studyEndReason, studyEndText,
        lodging);
    
    return student;
  }
  
  public Student archiveStudent(Student student, User user) {
    studentDAO.archive(student, user);
    return student;
  }
  
  public Student unarchiveStudent(Student student, User user) {
    studentDAO.unarchive(student, user);
    return student;
  }
}
