package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentExaminationTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
@Dependent
public class StudentExaminationTypeController {
  
  @Inject
  private StudentExaminationTypeDAO studentExaminationTypeDAO;
  
  public StudentExaminationType createStudentExaminationType(String name) {
    StudentExaminationType activityType = studentExaminationTypeDAO.create(name);
    return activityType;
  }
  
  public StudentExaminationType findStudentExaminationTypeById(Long id) {
    StudentExaminationType activityType = studentExaminationTypeDAO.findById(id);
    return activityType;
  }
  
  public List<StudentExaminationType> listStudentExaminationTypes() {
    List<StudentExaminationType> activityTypes = studentExaminationTypeDAO.listAll();
    return activityTypes;
  }

  public List<StudentExaminationType> listUnarchivedStudentExaminationTypes() {
    List<StudentExaminationType> activityTypes = studentExaminationTypeDAO.listUnarchived();
    return activityTypes;
  }
  
  public StudentExaminationType updateStudentExaminationType(StudentExaminationType activityType, String name) {
    StudentExaminationType updated = studentExaminationTypeDAO.updateName(activityType, name);
    return updated;
  }

  public StudentExaminationType archiveStudentExaminationType(StudentExaminationType studentExaminationType, User user) {
    studentExaminationTypeDAO.archive(studentExaminationType, user);
    return studentExaminationType;
  }

  public void deleteStudentExaminationType(StudentExaminationType studentExaminationType) {
    studentExaminationTypeDAO.delete(studentExaminationType);
  }

}
