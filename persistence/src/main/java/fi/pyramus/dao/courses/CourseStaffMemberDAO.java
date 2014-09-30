package fi.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStaffMember;
import fi.pyramus.domainmodel.courses.CourseStaffMember_;
import fi.pyramus.domainmodel.courses.CourseStaffMemberRole;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class CourseStaffMemberDAO extends PyramusEntityDAO<CourseStaffMember> {

  public CourseStaffMember create(Course course, User user, CourseStaffMemberRole role) {
    CourseStaffMember courseUser = new CourseStaffMember();
    courseUser.setCourse(course);
    courseUser.setUser(user);
    courseUser.setRole(role);
    return persist(courseUser);
  }

  public List<CourseStaffMember> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStaffMember> criteria = criteriaBuilder.createQuery(CourseStaffMember.class);
    Root<CourseStaffMember> root = criteria.from(CourseStaffMember.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseStaffMember_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public CourseStaffMember updateRole(CourseStaffMember courseStaffMember, CourseStaffMemberRole role) {
    courseStaffMember.setRole(role);
    return persist(courseStaffMember);
  }

}
