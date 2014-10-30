package fi.pyramus.services;

import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.StaffMember;

public class PyramusService {

  protected void validateEntity(Object entity) {
  	SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO(); 
  	  
  	Set<ConstraintViolation<Object>> constraintViolations = systemDAO.validateEntity(entity);
  	if (constraintViolations.size() != 0) {
  	  String message = "";
  	  for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
  	    message += constraintViolation.getMessage() + '\n';
  	  }  	
  		
  	  throw new PersistenceException(message);
  	}
  }
  
  public static boolean isAllowedEmail(String emailAddress) {
    return isAllowedEmail(emailAddress, null);
  }
  
  public static boolean isAllowedEmail(String emailAddress, Long personId) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    StaffMember staffMember = staffMemberDAO.findByEmail(emailAddress);
    List<Student> students = studentDAO.listByEmail(emailAddress);

    if (personId != null) {
      // True, if found matches the person, or if not found at all 
      boolean allowed = true;
      
      allowed = allowed && (staffMember != null ? personId.equals(staffMember.getPerson().getId()) : true);
      
      for (Student student : students)
        allowed = allowed && (student != null ? personId.equals(student.getPerson().getId()) : true);
      
      return allowed;
    } else {
      // True, if email is not in use
      return (staffMember == null) && (students.size() == 0);
    }
  }

}