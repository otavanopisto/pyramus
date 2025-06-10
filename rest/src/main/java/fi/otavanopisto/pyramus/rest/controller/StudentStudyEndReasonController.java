package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;

@Dependent
@Stateless
public class StudentStudyEndReasonController {

  @Inject
  private StudentStudyEndReasonDAO studentStudyEndReasonDAO;
  
  public StudentStudyEndReason createStudentStudyEndReason(StudentStudyEndReason parentReason, String name) {
    StudentStudyEndReason studentStudyEndReason = studentStudyEndReasonDAO.create(parentReason, name);
    return studentStudyEndReason;
  }
  
  public StudentStudyEndReason findStudentStudyEndReasonById(Long id) {
    StudentStudyEndReason studentStudyEndReason = studentStudyEndReasonDAO.findById(id);
    return studentStudyEndReason;
  }
  
  public List<StudentStudyEndReason> listStudentStudyEndReasons() {
    List<StudentStudyEndReason> endReasons = studentStudyEndReasonDAO.listAll();
    return endReasons;
  }
  
  public List<StudentStudyEndReason> listUnarchivedStudentStudyEndReasons() {
    List<StudentStudyEndReason> endReasons = studentStudyEndReasonDAO.listUnarchived();
    return endReasons;
  }
  
  public StudentStudyEndReason updateStudentStudyEndReason(StudentStudyEndReason endReason, String name) {
    StudentStudyEndReason studentStudyEndReason = studentStudyEndReasonDAO.updateName(endReason, name);
    return studentStudyEndReason;
  }
  
  public StudentStudyEndReason updateStudentStudyEndReasonParent(StudentStudyEndReason endReason, StudentStudyEndReason newParentReason) {
    StudentStudyEndReason studentStudyEndReason = studentStudyEndReasonDAO.updateParentReason(endReason, newParentReason);
    return studentStudyEndReason;
  }
  
  public void deleteStudentStudyEndReason(StudentStudyEndReason studentStudyEndReason) {
    studentStudyEndReasonDAO.delete(studentStudyEndReason);
  }
}
