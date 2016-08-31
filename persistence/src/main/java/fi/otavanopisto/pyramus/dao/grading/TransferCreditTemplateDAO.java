package fi.otavanopisto.pyramus.dao.grading;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplate;

@Stateless
public class TransferCreditTemplateDAO extends PyramusEntityDAO<TransferCreditTemplate> {

  public TransferCreditTemplate create(String name, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager();
    
    TransferCreditTemplate transferCreditTemplate = new TransferCreditTemplate();
    transferCreditTemplate.setName(name);
    transferCreditTemplate.setCurriculum(curriculum);
    
    entityManager.persist(transferCreditTemplate);
    
    return transferCreditTemplate;
  }
  
  public TransferCreditTemplate update(TransferCreditTemplate transferCreditTemplate, String name, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager();
    
    transferCreditTemplate.setName(name);
    transferCreditTemplate.setCurriculum(curriculum);
    
    entityManager.persist(transferCreditTemplate);
    
    return transferCreditTemplate;
  }
  
  @Override
  public void delete(TransferCreditTemplate transferCreditTemplate) {
    super.delete(transferCreditTemplate);
  }
  
}
