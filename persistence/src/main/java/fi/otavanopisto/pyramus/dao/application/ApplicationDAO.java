package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.application.Application_;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class ApplicationDAO extends PyramusEntityDAO<Application> {

  public Application create(
      String applicationId,
      String line,
      String firstName,
      String lastName,
      String email,
      String referenceCode,
      String formData,
      Boolean applicantEditable,
      ApplicationState state) {
    EntityManager entityManager = getEntityManager();

    Application application = new Application();
    
    application.setApplicationId(applicationId);
    application.setLine(line);
    application.setFirstName(firstName);
    application.setLastName(lastName);
    application.setEmail(email);
    application.setReferenceCode(referenceCode);
    application.setFormData(formData);
    application.setState(state);
    application.setCreated(new Date());
    application.setLastModified(new Date());
    application.setApplicantLastModified(new Date());
    application.setApplicantEditable(applicantEditable);
    application.setArchived(Boolean.FALSE);
   
    entityManager.persist(application);

    return application;
  }
  
  public Application update(
      Application application,
      String line,
      String firstName,
      String lastName,
      String email,
      String referenceCode,
      String formData,
      ApplicationState state,
      Boolean applicantEditable,
      User updatingUser) {
    EntityManager entityManager = getEntityManager();
    
    application.setLine(line);
    application.setFirstName(firstName);
    application.setLastName(lastName);
    application.setEmail(email);
    application.setReferenceCode(referenceCode);
    application.setFormData(formData);
    application.setState(state);
    application.setApplicantEditable(applicantEditable);
    if (updatingUser == null) {
      application.setApplicantLastModified(new Date());
    }
    else {
      application.setLastModifier(updatingUser);
      application.setLastModified(new Date());
    }
    entityManager.persist(application);

    return application;
  }
  
  public Application findByApplicationId(String applicationId) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> criteria = criteriaBuilder.createQuery(Application.class);
    Root<Application> root = criteria.from(Application.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(Application_.applicationId), applicationId)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public Application findByLastNameAndReferenceCode(String lastName, String referenceCode) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> criteria = criteriaBuilder.createQuery(Application.class);
    Root<Application> root = criteria.from(Application.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Application_.lastName), lastName),
        criteriaBuilder.equal(root.get(Application_.referenceCode), referenceCode)
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
