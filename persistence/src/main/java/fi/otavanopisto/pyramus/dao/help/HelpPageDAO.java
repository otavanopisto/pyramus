package fi.otavanopisto.pyramus.dao.help;

import java.util.Date;

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

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpFolder;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPage;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPageContent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

@Stateless
public class HelpPageDAO extends PyramusEntityDAO<HelpPage> {

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
    SearchSession session = Search.session(entityManager);

    QueryParser parser = new QueryParser("", new StandardAnalyzer());
    String queryString = queryBuilder.toString();
    Query luceneQuery;
    
    try {
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }
  
      LuceneSearchResult<HelpPage> fetch = session
          .search(HelpPage.class)
          .extension(LuceneExtension.get())
          .where(f -> f.fromLuceneQuery(luceneQuery))
          .sort(f -> 
              f.field("recursiveIndex"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
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
