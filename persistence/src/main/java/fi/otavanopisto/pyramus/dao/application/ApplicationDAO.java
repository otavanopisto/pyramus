package fi.otavanopisto.pyramus.dao.application;

import java.util.Date;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.application.Application_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.ApplicationCreatedEvent;
import fi.otavanopisto.pyramus.events.ApplicationModifiedByApplicantEvent;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class ApplicationDAO extends PyramusEntityDAO<Application> {

  @Inject
  private Event<ApplicationCreatedEvent> applicationCreatedEvent;

  @Inject
  private Event<ApplicationModifiedByApplicantEvent> applicationModifiedByApplicantEvent;

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
    
    applicationCreatedEvent.fire(new ApplicationCreatedEvent(application.getId()));

    return application;
  }
  
  @SuppressWarnings("unchecked")
  public SearchResult<Application> searchApplications(int resultsPerPage, int page, String line, ApplicationState state, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    if (!StringUtils.isBlank(line)) {
      addTokenizedSearchCriteria(queryBuilder, "line", line, true);
    }
    if (state != null) {
      addTokenizedSearchCriteria(queryBuilder, "state", state.toString(), true);
    }
    
    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Application.class)
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedApplication").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0)
        pages++;

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  public Application updateApplicationStateAsApplicant(Application application, ApplicationState applicationState) {
    EntityManager entityManager = getEntityManager();
    application.setState(applicationState);
    application.setApplicantLastModified(new Date());
    entityManager.persist(application);
    return application;
  }

  public Application updateApplicationState(Application application, ApplicationState applicationState, User user) {
    EntityManager entityManager = getEntityManager();
    application.setState(applicationState);
    application.setLastModifier(user);
    application.setLastModified(new Date());
    entityManager.persist(application);
    return application;
  }

  public Application updateApplicationHandler(Application application, StaffMember staffMember) {
    EntityManager entityManager = getEntityManager();
    application.setHandler(staffMember);
    application.setLastModifier(staffMember);
    application.setLastModified(new Date());
    entityManager.persist(application);
    return application;
  }
  
  public Application updateApplicantEditable(Application application, Boolean applicantEditable, User user) {
    EntityManager entityManager = getEntityManager();
    application.setApplicantEditable(applicantEditable);
    application.setLastModifier(user);
    application.setLastModified(new Date());
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
    
    if (updatingUser == null) {
      applicationModifiedByApplicantEvent.fire(new ApplicationModifiedByApplicantEvent(application.getId()));
    }

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
  
  public Application findByApplicationIdAndArchived(String applicationId, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Application> criteria = criteriaBuilder.createQuery(Application.class);
    Root<Application> root = criteria.from(Application.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(Application_.applicationId), applicationId),
        criteriaBuilder.equal(root.get(Application_.archived), archived)
      )
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
        criteriaBuilder.equal(criteriaBuilder.lower(root.get(Application_.lastName)), lastName.toLowerCase()),
        criteriaBuilder.equal(criteriaBuilder.upper(root.get(Application_.referenceCode)), referenceCode.toUpperCase())
      )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
