package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.pyramus.domainmodel.base.CourseEducationType;
import fi.pyramus.domainmodel.base.EducationSubtype;

@Stateless
public class CourseEducationSubtypeDAO extends PyramusEntityDAO<CourseEducationSubtype> {

  public CourseEducationSubtype create(CourseEducationType courseEducationType,
      EducationSubtype educationSubtype) {
    EntityManager entityManager = getEntityManager();
    CourseEducationSubtype courseEducationSubtype = new CourseEducationSubtype(educationSubtype);
    entityManager.persist(courseEducationSubtype);
    courseEducationType.addSubtype(courseEducationSubtype);
    entityManager.persist(courseEducationType);
    return courseEducationSubtype;
  }

}
