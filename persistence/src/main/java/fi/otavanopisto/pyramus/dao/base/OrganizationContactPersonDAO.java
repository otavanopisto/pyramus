package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPerson;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPersonType;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPerson_;

@Stateless
public class OrganizationContactPersonDAO extends PyramusEntityDAO<OrganizationContactPerson> {

  public OrganizationContactPerson create(Organization organization, OrganizationContactPersonType type, String name, String email, String phone, String title) {
    OrganizationContactPerson contactPerson = new OrganizationContactPerson();
    
    contactPerson.setOrganization(organization);
    contactPerson.setType(type);
    contactPerson.setName(name);
    contactPerson.setEmail(email);
    contactPerson.setPhone(phone);
    contactPerson.setTitle(title);

    return persist(contactPerson);
  }

  public OrganizationContactPerson update(OrganizationContactPerson contactPerson, OrganizationContactPersonType type, String name, String email, String phone, String title) {
    contactPerson.setType(type);
    contactPerson.setName(name);
    contactPerson.setEmail(email);
    contactPerson.setPhone(phone);
    contactPerson.setTitle(title);

    return persist(contactPerson);
  }

  public List<OrganizationContactPerson> listBy(Organization organization) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<OrganizationContactPerson> criteria = criteriaBuilder.createQuery(OrganizationContactPerson.class);
    Root<OrganizationContactPerson> root = criteria.from(OrganizationContactPerson.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(OrganizationContactPerson_.organization), organization)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(OrganizationContactPerson e) {
    super.delete(e);
  }

}