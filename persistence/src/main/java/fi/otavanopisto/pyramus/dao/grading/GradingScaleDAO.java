package fi.otavanopisto.pyramus.dao.grading;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;

@Stateless
public class GradingScaleDAO extends PyramusEntityDAO<GradingScale> {

  /**
   * Creates new GradingScale
   * 
   * @param name scale's name
   * @param description description for scale
   * @return GradingScale
   */
  public GradingScale create(String name, String description) {
    GradingScale gradingScale = new GradingScale();
    gradingScale.setName(name);
    gradingScale.setDescription(description);
    
    EntityManager entityManager = getEntityManager();
    entityManager.persist(gradingScale);
    
    return gradingScale;
  }
  
  /**
   * Updates GradingScale
   * 
   * @param name scale's name
   * @param description description for scale
   * @return GradingScale
   */
  public GradingScale update(GradingScale gradingScale, String name, String description) {
    gradingScale.setName(name);
    gradingScale.setDescription(description);
    
    EntityManager entityManager = getEntityManager();
    entityManager.persist(gradingScale);
    
    return gradingScale;
  }
  
  /**
   * Deletes a GradingScale
   * 
   * @param gradingScale GradingScale to be deleted
   */
  @Override
  public void delete(GradingScale gradingScale)  {
    super.delete(gradingScale);
  }
  
}
