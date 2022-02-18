package fi.otavanopisto.pyramus.dao.users;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo_;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType_;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Email_;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable_;
import fi.otavanopisto.pyramus.domainmodel.users.User_;
import fi.otavanopisto.pyramus.events.StaffMemberCreatedEvent;
import fi.otavanopisto.pyramus.events.StaffMemberDeletedEvent;
import fi.otavanopisto.pyramus.events.StaffMemberUpdatedEvent;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class StaffMemberDAO extends PyramusEntityDAO<StaffMember> {
  
  @Inject
  private Event<StaffMemberCreatedEvent> staffMemberCreatedEvent;

  @Inject
  private Event<StaffMemberUpdatedEvent> staffMemberUpdatedEvent;
  
  @Inject
  private Event<StaffMemberDeletedEvent> staffMemberDeletedEvent;
  
  public StaffMember create(Organization organization, String firstName, String lastName, Role role, Person person, Boolean archived) {
    ContactInfo contactInfo = new ContactInfo();
    
    StaffMember staffMember = new StaffMember();

    staffMember.setOrganization(organization);
    staffMember.setFirstName(firstName);
    staffMember.setLastName(lastName);
    staffMember.setRole(role);
    staffMember.setContactInfo(contactInfo);
    staffMember.setPerson(person);
    // TODO: allow archive on StaffMember
    staffMember.setArchived(archived);
    
    persist(staffMember);
    
    person.addUser(staffMember);
    getEntityManager().persist(person);
    
    staffMemberCreatedEvent.fire(new StaffMemberCreatedEvent(staffMember.getId()));

    return staffMember;
  }

  public List<StaffMember> listByUserVariable(String key, String value) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableKey userVariableKey = variableKeyDAO.findByVariableKey(key);

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<UserVariable> uvRoot = criteria.from(UserVariable.class);
    Root<StaffMember> smRoot = criteria.from(StaffMember.class);
    
    Join<UserVariable, User> userJoin = uvRoot.join(UserVariable_.user);
    
    criteria.select(smRoot);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(uvRoot.get(UserVariable_.user), smRoot),
            criteriaBuilder.equal(uvRoot.get(UserVariable_.key), userVariableKey),
            criteriaBuilder.equal(uvRoot.get(UserVariable_.value), value),
            criteriaBuilder.equal(userJoin.get(User_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<StaffMember> listByProperty(String key, String value) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    MapJoin<StaffMember, String, String> props = root.join(StaffMember_.properties);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StaffMember_.archived), Boolean.FALSE),
            criteriaBuilder.equal(props.key(), key),
            criteriaBuilder.equal(props.value(), value)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<StaffMember> listByNotRole(Role role) {
    return listByNotRole(role, null, null);
  }

  public List<StaffMember> listByNotRole(Role role, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    criteria.select(root);
    
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.notEqual(root.get(StaffMember_.role), role),
            criteriaBuilder.notEqual(root.get(StaffMember_.archived), Boolean.FALSE)
        )
    );
    
    TypedQuery<StaffMember> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
    
    return query.getResultList();
  }
  
  public List<StaffMember> listByOrganizationAndArchived(Organization organization, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(StaffMember_.organization), organization),
        criteriaBuilder.equal(root.get(StaffMember_.archived), archived)
      )
     );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<StaffMember> listByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StaffMember_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StaffMember_.person), person)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public StaffMember findByUniqueEmail(String email) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    
    Join<StaffMember, ContactInfo> contactInfoJoin = root.join(StaffMember_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    Join<Email, ContactType> contactTypeJoin = emailJoin.join(Email_.contactType);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(emailJoin.get(Email_.address), email),
            criteriaBuilder.equal(contactTypeJoin.get(ContactType_.nonUnique), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StaffMember_.archived), Boolean.FALSE)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }


  public StaffMember findByPerson(Person person) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StaffMember> criteria = criteriaBuilder.createQuery(StaffMember.class);
    Root<StaffMember> root = criteria.from(StaffMember.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StaffMember_.person), person),
            criteriaBuilder.equal(root.get(StaffMember_.archived), Boolean.FALSE)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StaffMember> searchUsersBasic(int resultsPerPage, int page, String text) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("+(");
    addTokenizedSearchCriteria(queryBuilder, "firstName", text, false);
    addTokenizedSearchCriteria(queryBuilder, "lastName", text, false);
    addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
    addTokenizedSearchCriteria(queryBuilder, "contactInfo.emails.address", text, false);
    queryBuilder.append(")");
  
    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      org.apache.lucene.search.Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StaffMember.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.Type.STRING),
                   new SortField("firstNameSortable", SortField.Type.STRING) }))
          .setFirstResult(firstResult)
          . setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StaffMember> searchUsers(int resultsPerPage, int page, String firstName, String lastName, String tags,
      String email, Role[] roles) {

    int firstResult = page * resultsPerPage;
    
    boolean hasFirstName = !StringUtils.isBlank(firstName);
    boolean hasLastName = !StringUtils.isBlank(lastName);
    boolean hasTags = !StringUtils.isBlank(tags);
    boolean hasEmail = !StringUtils.isBlank(email);
   
    StringBuilder queryBuilder = new StringBuilder();
    if (hasFirstName || hasLastName || hasEmail) {
      queryBuilder.append("+(");

      if (hasFirstName) 
        addTokenizedSearchCriteria(queryBuilder, "firstName", firstName, false);
      if (hasLastName)
        addTokenizedSearchCriteria(queryBuilder, "lastName", lastName, false);
      if (hasTags)
        addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, false);
      if (hasEmail)
        addTokenizedSearchCriteria(queryBuilder, "contactInfo.emails.address", email, false);

      queryBuilder.append(")");
    }

    if (roles.length > 0) {
      queryBuilder.append("+(");
      for (Role role : roles) {
        addTokenizedSearchCriteria(queryBuilder, "role", role.toString(), false);
      }
      queryBuilder.append(")");
    }
    
    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      org.apache.lucene.search.Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StaffMember.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.Type.STRING),
                   new SortField("firstNameSortable", SortField.Type.STRING) }))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  public StaffMember update(StaffMember staffMember, Organization organization, String firstName, String lastName, Role role) {
    auditUpdate(staffMember.getPersonId(), staffMember.getId(), staffMember, "firstName", firstName, true);
    auditUpdate(staffMember.getPersonId(), staffMember.getId(), staffMember, "lastName", firstName, true);
    auditUpdate(staffMember.getPersonId(), staffMember.getId(), staffMember, "role", role, true);

    staffMember.setOrganization(organization);
    staffMember.setFirstName(firstName);
    staffMember.setLastName(lastName);
    staffMember.setRole(role);
    persist(staffMember);
    
    staffMemberUpdatedEvent.fire(new StaffMemberUpdatedEvent(staffMember.getId()));
    
    return staffMember;
  }
  
  public void fireUpdate(Long staffMemberId) {
    staffMemberUpdatedEvent.fire(new StaffMemberUpdatedEvent(staffMemberId));
  }
  
  public StaffMember updateTags(StaffMember staffMember, Set<Tag> tags) {
    staffMember.setTags(tags);
    persist(staffMember);
    
    staffMemberUpdatedEvent.fire(new StaffMemberUpdatedEvent(staffMember.getId()));
    
    return staffMember;
  }
  
  public StaffMember updateTitle(StaffMember staffMember, String title) {
    staffMember.setTitle(title);
    persist(staffMember);
    
    staffMemberUpdatedEvent.fire(new StaffMemberUpdatedEvent(staffMember.getId()));
    
    return staffMember;
  }

  @Override
  public void delete(StaffMember user) {
    Long id = user.getId();
    
    if (user.getPerson() != null)
      user.getPerson().removeUser(user);

    super.delete(user);
    
    staffMemberDeletedEvent.fire(new StaffMemberDeletedEvent(id));
  }

}
