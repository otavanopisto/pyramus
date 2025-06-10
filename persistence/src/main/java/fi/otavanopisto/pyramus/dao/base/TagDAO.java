package fi.otavanopisto.pyramus.dao.base;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.base.Tag_;

@Stateless
public class TagDAO extends PyramusEntityDAO<Tag> {

  public Tag create(String text) {
    EntityManager entityManager = getEntityManager();

    Tag tag = new Tag();
    tag.setText(text);

    entityManager.persist(tag);

    return tag;
  }

  public Tag findByText(String text) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tag> criteria = criteriaBuilder.createQuery(Tag.class);
    Root<Tag> root = criteria.from(Tag.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(Tag_.text), text)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Tag updateText(Tag tag, String text) {
    EntityManager entityManager = getEntityManager();

    tag.setText(text);

    entityManager.persist(tag);

    return tag;
  }

  @Override
  public void delete(Tag tag) {
    super.delete(tag);
  }

}
