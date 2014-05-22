package fi.pyramus.dao.base;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Defaults;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseState;

@Stateless
public class DefaultsDAO extends PyramusEntityDAO<Defaults> {

  public boolean isPyramusInitialized() {
//    EntityManager entityManager = getEntityManager();
//    return s.get(Defaults.class, new Long(1)) != null;
    return getDefaults() != null;
  }

  public Defaults getDefaults() {
    return findById(new Long(1));
  }

  public Defaults updateDefaultBaseTimeUnit(EducationalTimeUnit defaultEducationalTimeUnit) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    defaults.setBaseTimeUnit(defaultEducationalTimeUnit);

    entityManager.persist(defaults);

    return defaults;
  }

  public Defaults updateInitialCourseParticipationType(CourseParticipationType initialCourseParticipationType) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    defaults.setInitialCourseParticipationType(initialCourseParticipationType);

    entityManager.persist(defaults);

    return defaults;
  }

  public Defaults updateDefaultInitialCourseState(CourseState initialCourseState) {
    EntityManager entityManager = getEntityManager();

    Defaults defaults = getDefaults();
    defaults.setInitialCourseState(initialCourseState);

    entityManager.persist(defaults);

    return defaults;
  }

}
