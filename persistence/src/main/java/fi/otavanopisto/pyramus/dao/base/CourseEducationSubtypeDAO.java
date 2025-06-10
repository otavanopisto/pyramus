package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.CourseEducationType;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;

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
