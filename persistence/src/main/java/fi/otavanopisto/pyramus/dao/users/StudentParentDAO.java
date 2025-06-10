package fi.otavanopisto.pyramus.dao.users;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo_;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType_;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Email_;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild_;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent_;
import fi.otavanopisto.pyramus.events.StudentParentCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentParentDeletedEvent;
import fi.otavanopisto.pyramus.events.StudentParentUpdatedEvent;

@Stateless
public class StudentParentDAO extends PyramusEntityDAO<StudentParent> {
  
  @Inject
  private Event<StudentParentCreatedEvent> studentParentCreatedEvent;

  @Inject
  private Event<StudentParentUpdatedEvent> studentParentUpdatedEvent;
  
  @Inject
  private Event<StudentParentDeletedEvent> studentParentDeletedEvent;
  
  public StudentParent create(String firstName, String lastName, Organization organization) {
    ContactInfo contactInfo = new ContactInfo();
    Person person = new Person();
    
    StudentParent studentParent = new StudentParent();

    studentParent.setFirstName(firstName);
    studentParent.setLastName(lastName);
    studentParent.setOrganization(organization);
    studentParent.setContactInfo(contactInfo);
    studentParent.setPerson(person);
    studentParent.setArchived(false);
    
    persist(studentParent);

    person.addUser(studentParent);
    person.setDefaultUser(studentParent);
    getEntityManager().persist(person);
    
    studentParentCreatedEvent.fire(new StudentParentCreatedEvent(studentParent.getId()));

    return studentParent;
  }

  public StudentParent findByUniqueEmail(String email) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParent> criteria = criteriaBuilder.createQuery(StudentParent.class);
    Root<StudentParent> root = criteria.from(StudentParent.class);
    
    Join<StudentParent, ContactInfo> contactInfoJoin = root.join(StudentParent_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    Join<Email, ContactType> contactTypeJoin = emailJoin.join(Email_.contactType);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(emailJoin.get(Email_.address), email),
            criteriaBuilder.equal(contactTypeJoin.get(ContactType_.nonUnique), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentParent_.archived), Boolean.FALSE)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<StudentParent> listBy(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParent> criteria = criteriaBuilder.createQuery(StudentParent.class);
    Root<StudentParent> root = criteria.from(StudentParent.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentParent_.person), person),
            criteriaBuilder.equal(root.get(StudentParent_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<StudentParent> listBy(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParent> criteria = criteriaBuilder.createQuery(StudentParent.class);
    Root<StudentParent> root = criteria.from(StudentParent.class);
    ListJoin<StudentParent, StudentParentChild> childJoin = root.join(StudentParent_.children);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(childJoin.get(StudentParentChild_.student), student),
            criteriaBuilder.equal(root.get(StudentParent_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public StudentParent update(StudentParent studentParent, String firstName, String lastName) {
    auditUpdate(studentParent.getPersonId(), studentParent.getId(), studentParent, StudentParent_.firstName, firstName, true);
    auditUpdate(studentParent.getPersonId(), studentParent.getId(), studentParent, StudentParent_.lastName, lastName, true);
    
    studentParent.setFirstName(firstName);
    studentParent.setLastName(lastName);
    
    studentParent = persist(studentParent);
    
    studentParentUpdatedEvent.fire(new StudentParentUpdatedEvent(studentParent.getId()));

    return studentParent;
  }

}
