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
  private StudentVariableKeyDAO studentVariableKeyDAO;
  
  /* StudentVariable */
  
  public StudentVariable createStudentVariable(Student student, StudentVariableKey key, String value) {
    StudentVariable studentVariable = studentVariableDAO.create(student, key, value);
    return studentVariable;
  }
  
  public StudentVariable finddStudentVariableById(Long id) {
    StudentVariable studentVariable = studentVariableDAO.findById(id);
    return studentVariable;
  }
  
  public List<StudentVariable> listStudentVariables() {
    List<StudentVariable> studentVariables = studentVariableDAO.listAll();
    return studentVariables;
  }
  
  public StudentVariable updateStudentVariable(StudentVariable studentVariable, String value) {
    StudentVariable updated = studentVariableDAO.update(studentVariable, value);
    return updated;
  }

  
  /* StudentVariableKey */
  
  public StudentVariableKey createStudentVariableKey(String key, String variableName, VariableType variableType, Boolean userEditable) {
    StudentVariableKey variableKey = studentVariableKeyDAO.create(userEditable, key, variableName, variableType);
    return variableKey;
  }
  
  public StudentVariableKey findStudentVariableKeyById(Long id) {
    StudentVariableKey variableKey = studentVariableKeyDAO.findById(id);
    return variableKey;
  }  
  
  public StudentVariableKey findStudentVariableKeyByVariableKey(String key) {
    return studentVariableKeyDAO.findByKey(key);
  }  

  public List<StudentVariableKey> listStudentVariableKeys() {
    return studentVariableKeyDAO.listAll();
  }
  
  public StudentVariableKey updateStudentVariableKey(StudentVariableKey studentVariableKey, String variableName, VariableType variableType, Boolean userEditable) {
    return 
      studentVariableKeyDAO.updateUserEditable(
        studentVariableKeyDAO.updateVariableName(
            studentVariableKeyDAO.updateVariableType(studentVariableKey, variableType), variableName), userEditable);
  }

  public void deleteStudentVariableKey(StudentVariableKey studentVariableKey) {
    studentVariableKeyDAO.delete(studentVariableKey);
  }
  
}
