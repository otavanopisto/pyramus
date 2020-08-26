package fi.otavanopisto.pyramus.dao.courses;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseSignupStudentGroup_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;

@Stateless
public class CourseSignupStudentGroupDAO extends PyramusEntityDAO<CourseSignupStudentGroup> {
  
  public CourseSignupStudentGroup create(Course course, StudentGroup studentGroup) {
    CourseSignupStudentGroup courseSignupStudentGroup = new CourseSignupStudentGroup();
    courseSignupStudentGroup.setCourse(course);
    courseSignupStudentGroup.setStudentGroup(studentGroup);
    return persist(courseSignupStudentGroup);
  }
  
  public List<CourseSignupStudentGroup> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseSignupStudentGroup> criteria = criteriaBuilder.createQuery(CourseSignupStudentGroup.class);
    Root<CourseSignupStudentGroup> root = criteria.from(CourseSignupStudentGroup.class);
    
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(CourseSignupStudentGroup_.course), course));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  @Override
  public void delete(CourseSignupStudentGroup courseSignupStudentGroup) {
    super.delete(courseSignupStudentGroup);
  }
}
