package fi.otavanopisto.pyramus.dao.help;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpFolder;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItem;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItemTitle;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItem_;

@Stateless
public class HelpItemDAO extends PyramusEntityDAO<HelpItem> {

  public List<HelpItem> listByParent(HelpFolder parent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<HelpItem> criteria = criteriaBuilder.createQuery(HelpItem.class);
    Root<HelpItem> root = criteria.from(HelpItem.class);
    criteria.select(root);
    
    if (parent == null) {
      criteria.where(
          criteriaBuilder.isNull(root.get(HelpItem_.parent))
      );
    } else {
      criteria.where(
          criteriaBuilder.equal(root.get(HelpItem_.parent), parent)
      );
    }

    return entityManager.createQuery(criteria).getResultList();
  }
  
  public void updateParent(HelpItem helpItem, HelpFolder parent) {
    EntityManager entityManager = getEntityManager();
    
    if (helpItem.getParent() != null) {
      HelpFolder oldParent = helpItem.getParent();
      oldParent.removeChild(helpItem);
      entityManager.persist(oldParent);
    } 

    helpItem.setParent(parent);
    
    entityManager.persist(helpItem);
  }
  
  @Override
  public void delete(HelpItem helpItem) {
    HelpItemTitleDAO helpItemTitleDAO = DAOFactory.getInstance().getHelpItemTitleDAO();
    
    HelpItemTitle[] titles = helpItem.getTitles().toArray(new HelpItemTitle[0]);
    for (int i = 0, l = titles.length; i < l; i++) {
      helpItemTitleDAO.delete(titles[i]);
    }
    
    super.delete(helpItem);
  }
  
}
