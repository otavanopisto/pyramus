package fi.pyramus.dao.projects;

import java.util.Date;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

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
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.SearchResult;

@Stateless
public class ProjectDAO extends PyramusEntityDAO<Project> {

  public Project create(String name, String description, Double optionalStudiesLength,
      EducationalTimeUnit optionalStudiesLengthTimeUnit, User creatingUser) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    Project project = new Project();
    project.setName(name);
    project.setDescription(description);
    project.getOptionalStudiesLength().setUnit(optionalStudiesLengthTimeUnit);
    project.getOptionalStudiesLength().setUnits(optionalStudiesLength);
    project.setCreator(creatingUser);
    project.setCreated(now);
    project.setLastModifier(creatingUser);
    project.setLastModified(now);

    entityManager.persist(project);

    return project;
  }
  
  public Project updateTags(Project project, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    project.setTags(tags);
    
    entityManager.persist(project);
    
    return project;
  }

  public Project update(Project project, String name, String description, Double optionalStudiesLength,
      EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    project.setName(name);
    project.setDescription(description);
    project.getOptionalStudiesLength().setUnit(optionalStudiesLengthTimeUnit);
    project.getOptionalStudiesLength().setUnits(optionalStudiesLength);
    project.setLastModifier(user);
    project.setLastModified(now);

    entityManager.persist(project);
    return project;
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Project> searchProjectsBasic(int resultsPerPage, int page, String text) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      queryBuilder.append(")");
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Project.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      query.enableFullTextFilter("ArchivedProject").setParameter("archived", Boolean.FALSE);
      
      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<Project>(page, pages, hits, firstResult, lastResult, query.getResultList());

    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  /**
   * Returns a list of projects matching the given search terms.
   * 
   * @param resultsPerPage
   *          The amount of projects per page
   * @param page
   *          The search results page
   * @param name
   *          The project name
   * @param description
   *          The project description
   * @param tags
   *          The projects tags
   * @param filterArchived
   *          <code>true</code> if archived projects should be omitted, otherwise <code>false</code>
   * 
   * @return A list of projects matching the given search terms
   */
  @SuppressWarnings("unchecked")
  public SearchResult<Project> searchProjects(int resultsPerPage, int page, String name, String description, String tags, boolean filterArchived) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    if (!StringUtils.isBlank(name)) {
      addTokenizedSearchCriteria(queryBuilder, "name", name, false);
    }
    if (!StringUtils.isBlank(description)) {
      addTokenizedSearchCriteria(queryBuilder, "description", description, false);
    }
    if (!StringUtils.isBlank(tags)) {
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, false);
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

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Project.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING) })).setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedProject").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<Project>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

}
