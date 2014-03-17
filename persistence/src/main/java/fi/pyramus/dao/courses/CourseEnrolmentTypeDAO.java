package fi.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;

@Stateless
public class CourseEnrolmentTypeDAO extends PyramusEntityDAO<CourseEnrolmentType> {

  public CourseEnrolmentType create(String name) {
    EntityManager entityManager = getEntityManager();

    CourseEnrolmentType courseEnrolmentType = new CourseEnrolmentType();
    courseEnrolmentType.setName(name);

    entityManager.persist(courseEnrolmentType);

    return courseEnrolmentType;
  }
  
  public CourseEnrolmentType updateName(CourseEnrolmentType courseEnrolmentType, String name) {
    EntityManager entityManager = getEntityManager();
    
    courseEnrolmentType.setName(name);
    
    entityManager.persist(courseEnrolmentType);
    return courseEnrolmentType;
  }
  
}
