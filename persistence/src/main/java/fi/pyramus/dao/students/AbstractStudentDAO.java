package fi.pyramus.dao.students;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.AbstractStudent_;
import fi.pyramus.domainmodel.students.Sex;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupStudent;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.persistence.search.StudentFilter;

@Stateless
public class AbstractStudentDAO extends PyramusEntityDAO<AbstractStudent> {

  /**
   * Creates new AbstractStudent instance and saves it to database
   * 
   * @param birthday
   *          student's birthday
   * @param socialSecurityNumber
   *          student's social security number
   * @param sex
   *          student's sex
   * @param secureInfo 
   * @return new instance of AbstractStudent
   */
  public AbstractStudent create(Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    EntityManager entityManager = getEntityManager();

    AbstractStudent abstractStudent = new AbstractStudent();
    abstractStudent.setBirthday(birthday);
    abstractStudent.setSocialSecurityNumber(socialSecurityNumber);
    abstractStudent.setSex(sex);
    abstractStudent.setBasicInfo(basicInfo);
    abstractStudent.setSecureInfo(secureInfo);
    entityManager.persist(abstractStudent);

    return abstractStudent;
  }
 
  public void update(AbstractStudent abstractStudent, Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    EntityManager entityManager = getEntityManager();
    abstractStudent.setBirthday(birthday);
    abstractStudent.setSocialSecurityNumber(socialSecurityNumber);
    abstractStudent.setSex(sex);
    abstractStudent.setBasicInfo(basicInfo);
    abstractStudent.setSecureInfo(secureInfo);
    entityManager.persist(abstractStudent);
  }

  /**
   * Returns an abstract student with the given social security number.
   * 
   * @param ssn
   *          The social security number
   * 
   * @return An abstract student with the given social security number
   */
  public AbstractStudent findBySSN(String ssn) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AbstractStudent> criteria = criteriaBuilder.createQuery(AbstractStudent.class);
    Root<AbstractStudent> root = criteria.from(AbstractStudent.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(AbstractStudent_.socialSecurityNumber), ssn));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public SearchResult<AbstractStudent> searchAbstractStudentsBasic(int resultsPerPage, int page, String queryText) {
    return searchAbstractStudentsBasic(resultsPerPage, page, queryText, StudentFilter.SKIP_INACTIVE);
  }

  public SearchResult<AbstractStudent> searchAbstractStudentsBasic(int resultsPerPage, int page, String queryText, StudentFilter studentFilter) {
    return searchAbstractStudentsBasic(resultsPerPage, page, queryText, studentFilter, null, null);
  }
  
  @SuppressWarnings("unchecked")
  public SearchResult<AbstractStudent> searchAbstractStudentsBasic(int resultsPerPage, int page, String queryText, StudentFilter studentFilter, StudyProgramme studyProgramme, StudentGroup studentGroup) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    switch (studentFilter) {
      case INCLUDE_INACTIVE:

        // Search should find past students as well, so an abstract student is considered
        // a match if it contains at least one non-archived student, no matter whether
        // their study end date has been set or not
        
        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, "activeFirstNames", "inactiveFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeNicknames", "inactiveNicknames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeLastNames", "inactiveLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeEmails", "inactiveEmails", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeTags", "inactiveTags", queryText, false);
          queryBuilder.append(")");
        }
        
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);

        queryBuilder.append("+(");
        addTokenizedSearchCriteria(queryBuilder, "active", "true", false, 0f);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", false, 0f);
        queryBuilder.append(")");
        
        // Other search terms
      break;
      case ONLY_INACTIVE:

        // Search should only find past students, so an abstract student is considered
        // a match if it only contains non-archived students who have their study end date
        // set and that date is in the past
        
        // Other search terms
        
        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, "inactiveFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveNicknames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveEmails", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "inactiveTags", queryText, false);
          queryBuilder.append(")");
        }

        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", studyProgramme.getId().toString(), true);

//        addTokenizedSearchCriteria(queryBuilder, "active", "false", true);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", true, 0f);
      break;
      case SKIP_INACTIVE:
        
        // Search should skip past students, so an abstract student is considered a match
        // if it contains at least one non-archived student who hasn't got his study end
        // date set or the date is in the future

        if (!StringUtils.isBlank(queryText)) {
          queryBuilder.append("+(");
          addTokenizedSearchCriteria(queryBuilder, "activeFirstNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeNicknames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeLastNames", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeEmails", queryText, false);
          addTokenizedSearchCriteria(queryBuilder, "activeTags", queryText, false);
          queryBuilder.append(")");
        }

        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);

        addTokenizedSearchCriteria(queryBuilder, "active", "true", true, 0f);
      break;
    }
    
    List<Long> studentIds = null;
    
    if (studentGroup != null) {
      studentIds = new ArrayList<Long>();
      
      switch (studentFilter) {
        case INCLUDE_INACTIVE:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            studentIds.add(sgs.getStudent().getId());
          }
        break;
        case ONLY_INACTIVE:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            if (!sgs.getStudent().getActive())
              studentIds.add(sgs.getStudent().getId());
          }
        break;
        case SKIP_INACTIVE:
          for (StudentGroupStudent sgs : studentGroup.getStudents()) {
            if (sgs.getStudent().getActive())
              studentIds.add(sgs.getStudent().getId());
          }
        break;
      }
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
    
    try {
      String queryString = queryBuilder.toString();
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      Query luceneQuery = parser.parse(queryString);

      FullTextQuery query = (FullTextQuery) fullTextEntityManager
          .createFullTextQuery(luceneQuery, AbstractStudent.class)
          .setSort(
              new Sort(new SortField[] { 
                  SortField.FIELD_SCORE, 
                  new SortField("lastNameSortable", SortField.STRING),
                  new SortField("firstNameSortable", SortField.STRING) })).setFirstResult(firstResult).setMaxResults(resultsPerPage);

      if (studentGroup != null)
        query.enableFullTextFilter("StudentIdFilter").setParameter("studentIds", studentIds);
      
      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<AbstractStudent>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<AbstractStudent> searchAbstractStudents(int resultsPerPage, int page, String firstName, String lastName, String nickname, String tags, 
      String education, String email, Sex sex, String ssn, String addressCity, String addressCountry, String addressPostalCode, String addressStreetAddress,
      String phone, Boolean lodging, StudyProgramme studyProgramme, Language language, Nationality nationality, Municipality municipality,
      StudentFilter studentFilter) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (sex != null)
      addTokenizedSearchCriteria(queryBuilder, "sex", sex.toString(), true);
    if (!StringUtils.isBlank(ssn))
      addTokenizedSearchCriteria(queryBuilder, "socialSecurityNumber", ssn, true);

    switch (studentFilter) {
      case INCLUDE_INACTIVE:

        // Search should find past students as well, so an abstract student is considered
        // a match if it contains at least one non-archived student, no matter whether
        // their study end date has been set or not
        
        queryBuilder.append("+(");
        addTokenizedSearchCriteria(queryBuilder, "active", "true", false);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", false);
        queryBuilder.append(")");
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveFirstNames", "activeFirstNames", firstName, true);
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveLastNames", "activeLastNames", lastName, true);
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, "inactiveNicknames", "activeNicknames", nickname, true);
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, "inactiveTags", "activeTags", tags, true);
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEducations", "activeEducations", education, true);
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEmails", "activeEmails", email, true);
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCities", "activeCities", addressCity, true);
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCountries", "activeCountries", addressCountry, true);
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, "inactivePostalCodes", "activePostalCodes", addressPostalCode, true);
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, "inactiveStreetAddresses", "activeStreetAddresses", addressStreetAddress, true);
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, "inactivePhones", "activePhones", phone, true);
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveNationalityIds", "activeNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveMunicipalityIds", "activeMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveLanguageIds", "activeLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "inactiveLodgings", "activeLodgings", lodging.toString(), true);
        }

      break;
      case ONLY_INACTIVE:

        // Search should only find past students, so an abstract student is considered
        // a match if it only contains non-archived students who have their study end date
        // set and that date is in the past
        
        addTokenizedSearchCriteria(queryBuilder, "active", "false", true);
        addTokenizedSearchCriteria(queryBuilder, "inactive", "true", true);
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveFirstNames", firstName, true);
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, "inactiveLastNames", lastName, true);
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, "inactiveNicknames", nickname, true);
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, "inactiveTags", tags, true);
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEducations", education, true);
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, "inactiveEmails", email, true);
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCities", addressCity, true);
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, "inactiveCountries", addressCountry, true);
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, "inactivePostalCodes", addressPostalCode, true);
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, "inactiveStreetAddresses", addressStreetAddress, true);
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, "inactivePhones", phone, true);
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "inactiveLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "inactiveLodgings", lodging.toString(), true);
        }
      break;
      case SKIP_INACTIVE:
        
        // Search should skip past students, so an abstract student is considered a match
        // if it contains at least one non-archived student who hasn't got his study end
        // date set or the date is in the future

        addTokenizedSearchCriteria(queryBuilder, "active", "true", true);
        
        // Other search terms
        
        if (!StringUtils.isBlank(firstName))
          addTokenizedSearchCriteria(queryBuilder, "activeFirstNames", firstName, true);
        if (!StringUtils.isBlank(lastName))
          addTokenizedSearchCriteria(queryBuilder, "activeLastNames", lastName, true);
        if (!StringUtils.isBlank(nickname))
          addTokenizedSearchCriteria(queryBuilder, "activeNicknames", nickname, true);
        if (!StringUtils.isBlank(education))
          addTokenizedSearchCriteria(queryBuilder, "activeEducations", education, true);
        if (!StringUtils.isBlank(tags))
          addTokenizedSearchCriteria(queryBuilder, "activeTags", tags, true);
        if (!StringUtils.isBlank(email))
          addTokenizedSearchCriteria(queryBuilder, "activeEmails", email, true);
        if (!StringUtils.isBlank(addressCity))
          addTokenizedSearchCriteria(queryBuilder, "activeCities", addressCity, true);
        if (!StringUtils.isBlank(addressCountry))
          addTokenizedSearchCriteria(queryBuilder, "activeCountries", addressCountry, true);
        if (!StringUtils.isBlank(addressPostalCode))
          addTokenizedSearchCriteria(queryBuilder, "activePostalCodes", addressPostalCode, true);
        if (!StringUtils.isBlank(addressStreetAddress))
          addTokenizedSearchCriteria(queryBuilder, "activeStreetAddresses", addressStreetAddress, true);
        if (!StringUtils.isBlank(phone))
          addTokenizedSearchCriteria(queryBuilder, "activePhones", phone, true);
        if (studyProgramme != null)
          addTokenizedSearchCriteria(queryBuilder, "activeStudyProgrammeIds", studyProgramme.getId().toString(), true);
        if (nationality != null)
          addTokenizedSearchCriteria(queryBuilder, "activeNationalityIds", nationality.getId().toString(), true);
        if (municipality != null)
          addTokenizedSearchCriteria(queryBuilder, "activeMunicipalityIds", municipality.getId().toString(), true);
        if (language != null)
          addTokenizedSearchCriteria(queryBuilder, "activeLanguageIds", language.getId().toString(), true);
        if (lodging != null) {
          addTokenizedSearchCriteria(queryBuilder, "activeLodgings", lodging.toString(), true);
        }
      break;
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager
          .createFullTextQuery(luceneQuery, AbstractStudent.class)
          .setFirstResult(firstResult);
      
      query.setSort(new Sort(
          new SortField[] { SortField.FIELD_SCORE, new SortField("lastNameSortable", SortField.STRING),
              new SortField("firstNameSortable", SortField.STRING) })).setMaxResults(resultsPerPage);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<AbstractStudent>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
}
