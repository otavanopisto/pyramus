package fi.otavanopisto.pyramus.dao.help;

import java.util.Date;
import java.util.Locale;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItem;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItemTitle;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItemTitle_;

@Stateless
public class HelpItemTitleDAO extends PyramusEntityDAO<HelpItemTitle> {

  public HelpItemTitle findByItemAndLocale(HelpItem item, Locale locale) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<HelpItemTitle> criteria = criteriaBuilder.createQuery(HelpItemTitle.class);
    Root<HelpItemTitle> root = criteria.from(HelpItemTitle.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(HelpItemTitle_.item), item),
            criteriaBuilder.equal(root.get(HelpItemTitle_.locale), locale)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public HelpItemTitle create(HelpItem item, Locale locale, String title, User creatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    HelpItemTitle helpItemTitle = new HelpItemTitle();
    helpItemTitle.setCreated(now);
    helpItemTitle.setLastModified(now);
    helpItemTitle.setCreator(creatingUser);
    helpItemTitle.setLastModifier(creatingUser);
    helpItemTitle.setLocale(locale);
    helpItemTitle.setTitle(title);
    
    entityManager.persist(helpItemTitle);
    
    item.addTitle(helpItemTitle);
    
    entityManager.persist(item);
    
    return helpItemTitle;
  }
  
  public void updateTitle(HelpItemTitle helpItemTitle, String title, User updatingUser) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    helpItemTitle.setLastModified(now);
    helpItemTitle.setLastModifier(updatingUser);
    helpItemTitle.setTitle(title);
    
    entityManager.persist(helpItemTitle);
  }
  
  @Override
  public void delete(HelpItemTitle helpItemTitle) {
    EntityManager entityManager = getEntityManager();
    
    if (helpItemTitle.getItem() != null) {
      HelpItem helpItem = helpItemTitle.getItem();
      helpItem.removeTitle(helpItemTitle);
      entityManager.persist(helpItem);
    }
      
    super.delete(helpItemTitle);
  }
  
}
