package fi.otavanopisto.pyramus.dao.projects;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

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
    SearchSession session = Search.session(entityManager);

    try {
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<Project> fetch = session
          .search(Project.class)
          .extension(LuceneExtension.get())
          .where(f -> 
            f.bool()
              .must(f.fromLuceneQuery(luceneQuery))
              .filter(f.match().field("archived").matching(Boolean.FALSE))
          )
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
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
    SearchSession session = Search.session(entityManager);
    
    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<Project> fetch = session
          .search(Project.class)
          .extension(LuceneExtension.get())
          .where(f -> f.bool().with(b -> {
            b.must(f.fromLuceneQuery(luceneQuery));

            if (filterArchived) {
              b.filter(f.match().field("archived").matching(Boolean.FALSE));
            }
          }))
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public Project removeTag(Project project, Tag tag) {
    project.removeTag(tag);
    return persist(project);
  }

}