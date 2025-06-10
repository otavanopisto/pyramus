package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;

@Stateless
public class EducationalTimeUnitDAO extends PyramusEntityDAO<EducationalTimeUnit> {

  /**
   * Creates a new educational time unit.
   * 
   * @param baseUnits
   *          The number of base units this unit is
   * @param name
   *          The unit name
   * @param symbol
   *          The unit symbol
   * @return The created education time unit
   */
  public EducationalTimeUnit create(Double baseUnits, String name, String symbol) {
    EntityManager entityManager = getEntityManager();

    EducationalTimeUnit educationalTimeUnit = new EducationalTimeUnit();
    educationalTimeUnit.setArchived(Boolean.FALSE);
    educationalTimeUnit.setBaseUnits(baseUnits);
    educationalTimeUnit.setName(name);
    educationalTimeUnit.setSymbol(symbol);

    entityManager.persist(educationalTimeUnit);

    return educationalTimeUnit;
  }

  public EducationalTimeUnit update(EducationalTimeUnit educationalTimeUnit, Double baseUnits, String name, String symbol) {
    EntityManager entityManager = getEntityManager();

    educationalTimeUnit.setBaseUnits(baseUnits);
    educationalTimeUnit.setName(name);
    educationalTimeUnit.setSymbol(symbol);

    entityManager.persist(educationalTimeUnit);

    return educationalTimeUnit;
  }

}
