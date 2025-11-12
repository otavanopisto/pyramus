package fi.otavanopisto.pyramus.dao.migration;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.migration.ContactInfoMigrationUser;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ContactInfoMigrationUserDAO extends PyramusEntityDAO<ContactInfoMigrationUser> {

  public List<ContactInfoMigrationUser> listBatch(int batchSize) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ContactInfoMigrationUser> criteria = criteriaBuilder.createQuery(ContactInfoMigrationUser.class);
    Root<ContactInfoMigrationUser> root = criteria.from(ContactInfoMigrationUser.class);
    
    criteria.select(root);
    return entityManager.createQuery(criteria).setMaxResults(batchSize).getResultList();
  }

  public void update(User user) {
    getEntityManager().persist(user);
  }

  public Email setAsDefault(Email email) {
    email.setDefaultAddress(Boolean.TRUE);
    getEntityManager().persist(email);
    return email;
  }
  
  public Address setAsDefault(Address address) {
    address.setDefaultAddress(Boolean.TRUE);
    getEntityManager().persist(address);
    return address;
  }
  
  public PhoneNumber setAsDefault(PhoneNumber phoneNumber) {
    phoneNumber.setDefaultNumber(Boolean.TRUE);
    getEntityManager().persist(phoneNumber);
    return phoneNumber;
  }
  
}