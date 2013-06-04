package fi.pyramus.dao.students;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupUser;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class StudentGroupUserDAO extends PyramusEntityDAO<StudentGroupUser> {

  public StudentGroupUser create(StudentGroup studentGroup, User user, User updatingUser) {
    EntityManager entityManager = getEntityManager();
    StudentGroupUser sgu = new StudentGroupUser();
    sgu.setUser(user);
    
    entityManager.persist(sgu);

    studentGroup.addUser(sgu);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    
    return sgu;
  }

  public void remove(StudentGroup studentGroup, StudentGroupUser user, User updatingUser) {
    EntityManager entityManager = getEntityManager(); 
    studentGroup.removeUser(user);

    studentGroup.setLastModifier(updatingUser);
    studentGroup.setLastModified(new Date(System.currentTimeMillis()));

    entityManager.persist(studentGroup);
    entityManager.remove(user);
  }
  
}
