package fi.otavanopisto.pyramus.dao.base;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContractPeriod;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContractPeriod_;

@Stateless
public class OrganizationContractPeriodDAO extends PyramusEntityDAO<OrganizationContractPeriod> {

  public OrganizationContractPeriod create(Organization organization, Date begin, Date end) {
    OrganizationContractPeriod contactPerson = new OrganizationContractPeriod();
    
    contactPerson.setOrganization(organization);
    contactPerson.setBegin(begin);
    contactPerson.setEnd(end);

    return persist(contactPerson);
  }

  public OrganizationContractPeriod update(OrganizationContractPeriod contractPeriod, Date begin, Date end) {
    contractPeriod.setBegin(begin);
    contractPeriod.setEnd(end);

    return persist(contractPeriod);
  }

  public List<OrganizationContractPeriod> listBy(Organization organization) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<OrganizationContractPeriod> criteria = criteriaBuilder.createQuery(OrganizationContractPeriod.class);
    Root<OrganizationContractPeriod> root = criteria.from(OrganizationContractPeriod.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(OrganizationContractPeriod_.organization), organization)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(OrganizationContractPeriod e) {
    super.delete(e);
  }

}