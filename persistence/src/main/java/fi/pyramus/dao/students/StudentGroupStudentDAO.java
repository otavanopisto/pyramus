package fi.pyramus.dao.students;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupStudent;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class StudentGroupStudentDAO extends PyramusEntityDAO<StudentGroupStudent> {

  public StudentGroupStudent create(StudentGroup studentGroup, Student student, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    StudentGroupStudent sgs = new StudentGroupStudent();
    sgs.setStudent(student);
    entityManager.persist(sgs);
    
    studentGroup.addStudent(sgs);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    
    return sgs;
  }
  
  public void update(StudentGroupStudent studentGroupStudent, Student student, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    StudentGroup studentGroup = studentGroupStudent.getStudentGroup();
    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));
    entityManager.persist(studentGroup);
    
    studentGroupStudent.setStudent(student);
    entityManager.persist(studentGroupStudent);
  }

  public void remove(StudentGroup studentGroup, StudentGroupStudent student, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    studentGroup.removeStudent(student);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    entityManager.remove(student);
  }
  
}
