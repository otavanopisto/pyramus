package fi.otavanopisto.pyramus.dao.help;

import java.util.Date;
import java.util.Locale;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPage;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPageContent;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPageContent_;

@Stateless
public class HelpPageContentDAO extends PyramusEntityDAO<HelpPageContent> {

  public HelpPageContent findByPageAndLocale(HelpPage page, Locale locale) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<HelpPageContent> criteria = criteriaBuilder.createQuery(HelpPageContent.class);
    Root<HelpPageContent> root = criteria.from(HelpPageContent.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(HelpPageContent_.page), page),
            criteriaBuilder.equal(root.get(HelpPageContent_.locale), locale)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public HelpPageContent create(HelpPage page, Locale locale, String content, User creatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    HelpPageContent helpPageContent = new HelpPageContent();
    helpPageContent.setCreated(now);
    helpPageContent.setLastModified(now);
    helpPageContent.setCreator(creatingUser);
    helpPageContent.setLastModifier(creatingUser);
    helpPageContent.setLocale(locale);
    helpPageContent.setContent(content);
    
    entityManager.persist(helpPageContent);
    
    page.addContent(helpPageContent);
    
    entityManager.persist(page);
    
    return helpPageContent;
  }
  
  public void updateContent(HelpPageContent helpPageContent, String content, User updatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    helpPageContent.setLastModified(now);
    helpPageContent.setLastModifier(updatingUser);
    helpPageContent.setContent(content);
    
    entityManager.persist(helpPageContent);
  }
  
  @Override
  public void delete(HelpPageContent helpPageContent) {
    EntityManager entityManager = getEntityManager();
    
    if (helpPageContent.getPage() != null) {
      HelpPage helpPage = helpPageContent.getPage();
      helpPage.removeContent(helpPageContent);
      entityManager.persist(helpPage);
    }
      
    super.delete(helpPageContent);
  }
}
