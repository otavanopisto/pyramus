package fi.pyramus.util.dataimport.scripting.api;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class StudentGroupStudentAPI {
  
  public StudentGroupStudentAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(Long studentId, Long studentGroupId) throws InvalidScriptException {
    User loggedUser = DAOFactory.getInstance().getUserDAO().findById(loggedUserId);
    if (loggedUser == null) {
      throw new InvalidScriptException("Logged user could not be found");  
    }

    Student student =  DAOFactory.getInstance().getStudentDAO().findById(studentId);
    if (student == null) {
      throw new InvalidScriptException("Could not find student #" + studentId);  
    }
    
    StudentGroup studentGroup = DAOFactory.getInstance().getStudentGroupDAO().findById(studentGroupId);
    if (studentGroup == null) {
      throw new InvalidScriptException("Could not find student group #" + studentGroupId);  
    }
    
    return (DAOFactory.getInstance().getStudentGroupStudentDAO()).create(studentGroup, student, loggedUser).getId();
  }

  private Long loggedUserId;
}
