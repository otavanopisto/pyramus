package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.students.StudentVariableDAO;
import fi.pyramus.dao.students.StudentVariableKeyDAO;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentVariable;
import fi.pyramus.domainmodel.students.StudentVariableKey;

@Dependent
@Stateless
public class StudentVariableController {
  @Inject
  private StudentVariableDAO studentVariableDAO;
  @Inject
  private StudentVariableKeyDAO variableKeyDAO;
  
  public StudentVariable createStudentVariable(Student student, StudentVariableKey key, String value) {
    StudentVariable studentVariable = studentVariableDAO.create(student, key, value);
    return studentVariable;
  }
  
  public StudentVariableKey createStudentVariableKey(boolean userEditable, String key, String variableName, VariableType variableType) {
    StudentVariableKey variableKey = variableKeyDAO.create(userEditable, key, variableName, variableType);
    return variableKey;
  }
  
  public List<StudentVariable> findStudentVariables() {
    List<StudentVariable> studentVariables = studentVariableDAO.listAll();
    return studentVariables;
  }
  
  public StudentVariable findStudentVariableById(Long id) {
    StudentVariable studentVariable = studentVariableDAO.findById(id);
    return studentVariable;
  }
  
  public StudentVariableKey findStudentVariableKeyById(Long id) {
    StudentVariableKey variableKey = variableKeyDAO.findById(id);
    return variableKey;
  }
  
  public StudentVariable updateStudentVariable(StudentVariable studentVariable, String value) {
    StudentVariable updated = studentVariableDAO.update(studentVariable, value);
    return updated;
  }
  

}
