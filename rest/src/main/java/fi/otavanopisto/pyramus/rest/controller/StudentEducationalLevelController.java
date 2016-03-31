package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentEducationalLevelDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentEducationalLevel;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudentEducationalLevelController {
  
  @Inject
  private StudentEducationalLevelDAO studentEducationalLevelDAO;
  
  public StudentEducationalLevel createStudentEducationalLevel(String name) {
    StudentEducationalLevel activityType = studentEducationalLevelDAO.create(name);
    return activityType;
  }
  
  public StudentEducationalLevel findStudentEducationalLevelById(Long id) {
    StudentEducationalLevel activityType = studentEducationalLevelDAO.findById(id);
    return activityType;
  }
  
  public List<StudentEducationalLevel> listStudentEducationalLevels() {
    List<StudentEducationalLevel> activityTypes = studentEducationalLevelDAO.listAll();
    return activityTypes;
  }

  public List<StudentEducationalLevel> listUnarchivedStudentEducationalLevels() {
    List<StudentEducationalLevel> activityTypes = studentEducationalLevelDAO.listUnarchived();
    return activityTypes;
  }
  
  public StudentEducationalLevel updateStudentEducationalLevel(StudentEducationalLevel activityType, String name) {
    StudentEducationalLevel updated = studentEducationalLevelDAO.updateName(activityType, name);
    return updated;
  }

  public StudentEducationalLevel archiveStudentEducationalLevel(StudentEducationalLevel studentEducationalLevel, User user) {
    studentEducationalLevelDAO.archive(studentEducationalLevel, user);
    return studentEducationalLevel;
  }

  public void deleteStudentEducationalLevel(StudentEducationalLevel studentEducationalLevel) {
    studentEducationalLevelDAO.delete(studentEducationalLevel);
  }

}
