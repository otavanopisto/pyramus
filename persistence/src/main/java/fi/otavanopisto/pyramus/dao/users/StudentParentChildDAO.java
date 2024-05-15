package fi.otavanopisto.pyramus.dao.users;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;

@Stateless
public class StudentParentChildDAO extends PyramusEntityDAO<StudentParentChild> {
  
  public StudentParentChild create(StudentParent parent, Student child) {
    StudentParentChild studentParentChild = new StudentParentChild();

    studentParentChild.setStudentParent(parent);
    studentParentChild.setStudent(child);
    
    return persist(studentParentChild);
  }

}
