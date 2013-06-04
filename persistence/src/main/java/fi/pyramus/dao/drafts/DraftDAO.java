package fi.pyramus.dao.drafts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.drafts.FormDraft;
import fi.pyramus.domainmodel.drafts.FormDraft_;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class DraftDAO extends PyramusEntityDAO<FormDraft> {
  
  public FormDraft create(User creator, String url, String draftData) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    FormDraft formDraft = new FormDraft();
    formDraft.setUrl(url);
    formDraft.setData(draftData);
    formDraft.setCreated(now);
    formDraft.setModified(now);
    formDraft.setCreator(creator);
    
    entityManager.persist(formDraft);
    
    return formDraft;
  }
  
  public FormDraft findByUserAndURL(User creator, String url) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FormDraft> criteria = criteriaBuilder.createQuery(FormDraft.class);
    Root<FormDraft> root = criteria.from(FormDraft.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(FormDraft_.url), url),
            criteriaBuilder.equal(root.get(FormDraft_.creator), creator)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public void removeDeprecatedDrafts() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.roll(Calendar.DATE, -14);
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<FormDraft> criteria = criteriaBuilder.createQuery(FormDraft.class);
    Root<FormDraft> root = criteria.from(FormDraft.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.lessThan(root.get(FormDraft_.modified), c.getTime())
    );
    
    List<FormDraft> formDrafts = entityManager.createQuery(criteria).getResultList();
    
    for (FormDraft formDraft : formDrafts) {
      delete(formDraft);
    }
  }
  
  public FormDraft update(FormDraft formDraft, String draftData) {
    EntityManager entityManager = getEntityManager();
    
    Date now = new Date(System.currentTimeMillis());
    
    formDraft.setData(draftData);
    formDraft.setModified(now);
    
    entityManager.persist(formDraft);
    
    return formDraft;
  }
  
  @Override
  public void delete(FormDraft formDraft) {
    super.delete(formDraft);
  }
  
}
