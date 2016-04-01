package fi.otavanopisto.pyramus.dao.base;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;

@Stateless
public class AcademicTermDAO extends PyramusEntityDAO<AcademicTerm> {

  /**
   * Creates a new academic term.
   * 
   * @param name
   *          The name of the academic term
   * @param startDate
   *          The beginning date of the academic term
   * @param endDate
   *          The ending date of the academic term
   * 
   * @return The created academic term
   */
  public AcademicTerm create(String name, Date startDate, Date endDate) {
    EntityManager entityManager = getEntityManager();

    AcademicTerm term = new AcademicTerm();
    term.setName(name);
    term.setStartDate(startDate);
    term.setEndDate(endDate);
    entityManager.persist(term);

    return term;
  }

  /**
   * Updates the given academic term with the given data.
   * 
   * @param term
   *          The academic term to be updated
   * @param name
   *          The academic term name
   * @param startDate
   *          The academic term beginning date
   * @param endDate
   *          The academic term end date
   */
  public void update(AcademicTerm term, String name, Date startDate, Date endDate) {
    EntityManager entityManager = getEntityManager();

    term.setName(name);
    term.setStartDate(startDate);
    term.setEndDate(endDate);

    entityManager.persist(term);
  }

}
