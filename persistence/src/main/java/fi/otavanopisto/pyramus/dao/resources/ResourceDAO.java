package fi.otavanopisto.pyramus.dao.resources;

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

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.resources.MaterialResource;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceType;
import fi.otavanopisto.pyramus.domainmodel.resources.WorkResource;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class ResourceDAO extends PyramusEntityDAO<Resource> {

  @SuppressWarnings("unchecked")
  public SearchResult<Resource> searchResourcesBasic(int resultsPerPage, int page, String queryText) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(queryText)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", queryText, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", queryText, false);
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
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, WorkResource.class, MaterialResource.class)
        .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
        .setFirstResult(firstResult)
        .setMaxResults(resultsPerPage);
    
      query.setFirstResult(firstResult).setMaxResults(resultsPerPage);

      query.enableFullTextFilter("ArchivedResource").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = firstResult + resultsPerPage - 1;
      if (lastResult > hits - 1) {
        lastResult = hits - 1;
      }

      return new SearchResult<Resource>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Resource> searchResources(int resultsPerPage, int page, String name, String tags, ResourceType resourceType,
      ResourceCategory resourceCategory, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(name)) {
      addTokenizedSearchCriteria(queryBuilder, "name", name, true);
    }

    if (!StringUtils.isBlank(tags)) {
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, true);
    }
    
    if (resourceCategory != null) {
      addTokenizedSearchCriteria(queryBuilder, "category.id", resourceCategory.getId().toString(), true);
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {

      QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = null;

      if (resourceType == null) {
        query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, WorkResource.class, MaterialResource.class)
            .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
            .setFirstResult(firstResult)
            .setMaxResults(resultsPerPage);
      }
      else {
        switch (resourceType) {
        case MATERIAL_RESOURCE:
          query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, MaterialResource.class)
              .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
              .setFirstResult(firstResult)
              .setMaxResults(resultsPerPage);
          break;
        case WORK_RESOURCE:
          query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, WorkResource.class)
              .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.STRING)}))
              .setFirstResult(firstResult)
              .setMaxResults(resultsPerPage);
          break;
          default:
            throw new PersistenceException("Invalid resource type");
        }
          
      }
      
      query.setFirstResult(firstResult).setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedResource").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = firstResult + resultsPerPage - 1;
      if (lastResult > hits - 1) {
        lastResult = hits - 1;
      }

      return new SearchResult<Resource>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  public void setResourceTags(Resource resource, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    resource.setTags(tags);
    
    entityManager.persist(resource);
  }

  public void archiveResource(Resource resource) {
    EntityManager entityManager = getEntityManager();
    resource.setArchived(Boolean.TRUE);
    entityManager.persist(resource);
  }

  public void unarchiveResource(Resource resource) {
    EntityManager entityManager = getEntityManager();
    resource.setArchived(Boolean.FALSE);
    entityManager.persist(resource);
  }

  @Override
  public void delete(Resource resource) {
    super.delete(resource);
  }
  
}
