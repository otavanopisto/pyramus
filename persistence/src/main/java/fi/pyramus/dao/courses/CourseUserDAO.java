package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseUser;
import fi.pyramus.domainmodel.courses.CourseUserRole;
import fi.pyramus.domainmodel.courses.CourseUser_;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class CourseUserDAO extends PyramusEntityDAO<CourseUser> {

  public CourseUser create(Course course, User user, CourseUserRole role) {
    EntityManager entityManager = getEntityManager();

    CourseUser courseUser = new CourseUser();
    courseUser.setCourse(course);
    courseUser.setUser(user);
    courseUser.setRole(role);
    entityManager.persist(courseUser);

    course.addCourseUser(courseUser);
    entityManager.persist(course);

    return courseUser;
  }

  public List<CourseUser> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseUser> criteria = criteriaBuilder.createQuery(CourseUser.class);
    Root<CourseUser> root = criteria.from(CourseUser.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseUser_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public void update(CourseUser courseUser, User user, CourseUserRole role) {
    EntityManager entityManager = getEntityManager();
    courseUser.setUser(user);
    courseUser.setRole(role);
    entityManager.persist(courseUser);
  }

  @Override
  public void delete(CourseUser courseUser) {
    EntityManager entityManager = getEntityManager();
    Course course = courseUser.getCourse();
    course.removeCourseUser(courseUser);
    entityManager.persist(course);
    entityManager.remove(courseUser);
  }


}
