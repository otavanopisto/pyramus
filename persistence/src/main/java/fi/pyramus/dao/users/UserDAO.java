package fi.pyramus.dao.users;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.BillingDetails;
import fi.pyramus.domainmodel.base.ContactInfo;
import fi.pyramus.domainmodel.base.ContactInfo_;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.Email_;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.users.UserVariable;
import fi.pyramus.domainmodel.users.UserVariableKey;
import fi.pyramus.domainmodel.users.UserVariable_;
import fi.pyramus.domainmodel.users.User_;
import fi.pyramus.persistence.search.SearchResult;

@Stateless
public class UserDAO extends PyramusEntityDAO<User> {
  
  public User create(String firstName, String lastName, String externalId, String authProvider, Role role) {
    ContactInfo contactInfo = new ContactInfo();
    
    User newUser = new User();
    
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setAuthProvider(authProvider);
    newUser.setExternalId(externalId);
    newUser.setRole(role);
    newUser.setContactInfo(contactInfo);

    return persist(newUser);
  }

  public List<User> listByUserVariable(String key, String value) {
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    UserVariableKey userVariableKey = variableKeyDAO.findByVariableKey(key);

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<UserVariable> root = criteria.from(UserVariable.class);
    criteria.select(root.get(UserVariable_.user));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(UserVariable_.key), userVariableKey),
            criteriaBuilder.equal(root.get(UserVariable_.value), value)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public User findByExternalIdAndAuthProvider(String externalId, String authProvider) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(User_.externalId), externalId),
            criteriaBuilder.equal(root.get(User_.authProvider), authProvider)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public User findByEmail(String email) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
    Root<User> root = criteria.from(User.class);
    
    Join<User, ContactInfo> contactInfoJoin = root.join(User_.contactInfo);
    ListJoin<ContactInfo, Email> emailJoin = contactInfoJoin.join(ContactInfo_.emails);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(emailJoin.get(Email_.address), email)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }


  @SuppressWarnings("unchecked")
  public SearchResult<User> searchUsersBasic(int resultsPerPage, int page, String text) {

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
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, User.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.STRING),
                   new SortField("firstNameSortable", SortField.STRING) }))
          .setFirstResult(firstResult)
          . setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<User>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<User> searchUsers(int resultsPerPage, int page, String firstName, String lastName, String tags,
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
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, User.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.STRING),
                   new SortField("firstNameSortable", SortField.STRING) }))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<User>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  public User update(User user, String firstName, String lastName, Role role) {
    EntityManager entityManager = getEntityManager();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setRole(role);
    entityManager.persist(user);
    return user;
  }

  public User updateBillingDetails(User user, List<BillingDetails> billingDetails) {
    EntityManager entityManager = getEntityManager();
    
    user.setBillingDetails(billingDetails);
    
    entityManager.persist(user);
    
    return user;
  }
  
  public User updateTags(User user, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    user.setTags(tags);
    
    entityManager.persist(user);
    
    return user;
  }
  
  public User updateTitle(User user, String title) {
    EntityManager entityManager = getEntityManager();
    user.setTitle(title);
    entityManager.persist(user);
    return user;
  }

  public void updateAuthProvider(User user, String authProvider) {
    EntityManager entityManager = getEntityManager();
    
    user.setAuthProvider(authProvider);
    
    entityManager.persist(user);
  }

  public void updateExternalId(User user, String externalId) {
    EntityManager entityManager = getEntityManager();
    user.setExternalId(externalId);
    entityManager.persist(user);
  }

  @Override
  public void delete(User user) {
    super.delete(user);
  }

}
