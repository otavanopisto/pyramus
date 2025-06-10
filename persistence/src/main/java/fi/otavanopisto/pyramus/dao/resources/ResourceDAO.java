package fi.otavanopisto.pyramus.dao.resources;

import java.util.Collection;
import java.util.List;
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
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.resources.MaterialResource;
import fi.otavanopisto.pyramus.domainmodel.resources.Resource;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceCategory;
import fi.otavanopisto.pyramus.domainmodel.resources.ResourceType;
import fi.otavanopisto.pyramus.domainmodel.resources.WorkResource;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

@Stateless
public class ResourceDAO extends PyramusEntityDAO<Resource> {

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
    SearchSession session = Search.session(entityManager);

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

      LuceneSearchResult<Resource> fetch = session
          .search(List.of(WorkResource.class, MaterialResource.class))
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

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

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
    
    EntityManager entityManager = getEntityManager();
    SearchSession session = Search.session(entityManager);

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

      Collection<Class<? extends Resource>> from = List.of(WorkResource.class, MaterialResource.class);
      
      if (resourceType != null) {
        switch (resourceType) {
          case MATERIAL_RESOURCE:
            from = List.of(MaterialResource.class);
          break;
          case WORK_RESOURCE:
            from = List.of(WorkResource.class);
          break;
          default:
            throw new PersistenceException("Invalid resource type");
        }
          
      }
      
      LuceneSearchResult<Resource> fetch = session
          .search(from)
          .extension(LuceneExtension.get())
          .where(f -> f.bool().with(b -> {
            b.must(f.fromLuceneQuery(luceneQuery));

            if (resourceCategory != null) {
              b.filter(f.match().field("category.id").matching(resourceCategory.getId()));
            }
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
