package fi.pyramus.dao.courses;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.CourseState;

@Stateless
public class CourseStateDAO extends PyramusEntityDAO<CourseState> {
  
  public CourseState create(String name) {
    EntityManager entityManager = getEntityManager();

    CourseState courseState = new CourseState();
    courseState.setName(name);
    
    entityManager.persist(courseState);
    
    return courseState;
  }

  public CourseState update(CourseState courseState, String name) {
    EntityManager entityManager = getEntityManager();
    
    courseState.setName(name);

    entityManager.persist(courseState);
    
    return courseState;
  }

}
