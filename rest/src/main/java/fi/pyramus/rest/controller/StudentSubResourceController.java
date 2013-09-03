package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.students.StudentStudyEndReasonDAO;
import fi.pyramus.domainmodel.students.StudentStudyEndReason;

@Dependent
@Stateless
public class StudentSubResourceController {
  @Inject
  StudentStudyEndReasonDAO endReasonDAO;
  
  public StudentStudyEndReason createStudentStudyEndReason(StudentStudyEndReason parentReason, String name) {
    StudentStudyEndReason studentStudyEndReason = endReasonDAO.create(parentReason, name);
    return studentStudyEndReason;
  }
  
  public List<StudentStudyEndReason> findStudentStudyEndReasons() {
    List<StudentStudyEndReason> endReasons = endReasonDAO.listAll();
    return endReasons;
  }
  
  public List<StudentStudyEndReason> findUnarchivedStudentStudyEndReasons() {
    List<StudentStudyEndReason> endReasons = endReasonDAO.listUnarchived();
    return endReasons;
  }
  
  public StudentStudyEndReason findStudentStudyEndReasonById(Long id) {
    StudentStudyEndReason studentStudyEndReason = endReasonDAO.findById(id);
    return studentStudyEndReason;
  }
  
  public StudentStudyEndReason updateStudentStudyEndReason(StudentStudyEndReason endReason, String name) {
    StudentStudyEndReason studentStudyEndReason = endReasonDAO.updateName(endReason, name);
    return studentStudyEndReason;
  }
  
  public StudentStudyEndReason updateStudentStudyEndReasonParent(StudentStudyEndReason endReason, StudentStudyEndReason newParentReason) {
    StudentStudyEndReason studentStudyEndReason = endReasonDAO.updateParentReason(endReason, newParentReason);
    return studentStudyEndReason;
  }
  
}
