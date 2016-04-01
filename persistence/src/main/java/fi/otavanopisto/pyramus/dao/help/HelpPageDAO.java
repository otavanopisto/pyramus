package fi.otavanopisto.pyramus.dao.help;

import java.util.Date;

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

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpFolder;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPage;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPageContent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class HelpPageDAO extends PyramusEntityDAO<HelpPage> {

  @SuppressWarnings("unchecked")
  public SearchResult<HelpPage> searchHelpPagesBasic(int resultsPerPage, int page, String text) {
    int firstResult = page * resultsPerPage;
    StringBuilder queryBuilder = new StringBuilder();
    
    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "titles.title", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      addTokenizedSearchCriteria(queryBuilder, "contents.content", text, false);
      queryBuilder.append(")");
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    QueryParser parser = new QueryParser(Version.LUCENE_29, "", new StandardAnalyzer(Version.LUCENE_29));
    String queryString = queryBuilder.toString();
    Query luceneQuery;
    
    try {
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }
  
      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, HelpPage.class)
          .setSort(new Sort(new SortField[]{ new SortField("recursiveIndex", SortField.STRING) }))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);
  
      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }
  
      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;
  
      return new SearchResult<HelpPage>(page, pages, hits, firstResult, lastResult, query.getResultList());
    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
   
  public HelpPage create(HelpFolder parent, Integer indexColumn, User creatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    HelpPage helpPage = new HelpPage();
    helpPage.setCreated(now);
    helpPage.setLastModified(now);
    helpPage.setCreator(creatingUser);
    helpPage.setLastModifier(creatingUser);
    helpPage.setParent(parent);
    helpPage.setIndexColumn(indexColumn);
    
    entityManager.persist(helpPage);
    
    return helpPage;
  }
  
  @Override
  public void delete(HelpPage helpPage) {
    HelpPageContentDAO helpPageContentDAO = DAOFactory.getInstance().getHelpPageContentDAO();
    HelpItemDAO helpItemDAO = DAOFactory.getInstance().getHelpItemDAO();
    
    HelpPageContent[] contents = helpPage.getContents().toArray(new HelpPageContent[0]);
    for (int i = 0, l = contents.length; i < l; i++) {
      helpPageContentDAO.delete(contents[i]);
    }
    
    helpItemDAO.delete(helpPage);
  }
  
}
