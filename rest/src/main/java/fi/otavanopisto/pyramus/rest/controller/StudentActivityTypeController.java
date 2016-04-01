package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentActivityTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudentActivityTypeController {
  
  @Inject
  private StudentActivityTypeDAO studentActivityTypeDAO;
  
  public StudentActivityType createStudentActivityType(String name) {
    StudentActivityType activityType = studentActivityTypeDAO.create(name);
    return activityType;
  }
  
  public StudentActivityType findStudentActivityTypeById(Long id) {
    StudentActivityType activityType = studentActivityTypeDAO.findById(id);
    return activityType;
  }
  
  public List<StudentActivityType> listStudentActivityTypes() {
    List<StudentActivityType> activityTypes = studentActivityTypeDAO.listAll();
    return activityTypes;
  }

  public List<StudentActivityType> listUnarchivedStudentActivityTypes() {
    List<StudentActivityType> activityTypes = studentActivityTypeDAO.listUnarchived();
    return activityTypes;
  }
  
  public StudentActivityType updateStudentActivityType(StudentActivityType activityType, String name) {
    StudentActivityType updated = studentActivityTypeDAO.updateName(activityType, name);
    return updated;
  }

  public StudentActivityType archiveStudentActivityType(StudentActivityType studentActivityType, User user) {
    studentActivityTypeDAO.archive(studentActivityType, user);
    return studentActivityType;
  }

  public void deleteStudentActivityType(StudentActivityType studentActivityType) {
    studentActivityTypeDAO.delete(studentActivityType);
  }

}
