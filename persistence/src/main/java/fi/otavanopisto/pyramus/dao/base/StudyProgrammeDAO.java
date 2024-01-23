package fi.otavanopisto.pyramus.dao.base;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.StudyProgrammeArchivedEvent;
import fi.otavanopisto.pyramus.events.StudyProgrammeCreatedEvent;
import fi.otavanopisto.pyramus.events.StudyProgrammeUpdatedEvent;

@Stateless
public class StudyProgrammeDAO extends PyramusEntityDAO<StudyProgramme> {

  @Inject
  private Event<StudyProgrammeCreatedEvent> studyProgrammeCreatedEvent;
  
  @Inject
  private Event<StudyProgrammeUpdatedEvent> studyProgrammeUpdatedEvent;
  
  @Inject
  private Event<StudyProgrammeArchivedEvent> studyProgrammeRemovedEvent;

  public StudyProgramme create(Organization organization, String name, StudyProgrammeCategory category, String code, String officialEducationType, boolean hasEvaluationFees) {
    EntityManager entityManager = getEntityManager();

    StudyProgramme studyProgramme = new StudyProgramme();
    studyProgramme.setOrganization(organization);
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    studyProgramme.setOfficialEducationType(officialEducationType);
    studyProgramme.setHasEvaluationFees(hasEvaluationFees);
    entityManager.persist(studyProgramme);
    
    studyProgrammeCreatedEvent.fire(new StudyProgrammeCreatedEvent(studyProgramme.getId()));

    return studyProgramme;
  }

  public StudyProgramme findByName(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.name), name)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public StudyProgramme findByCode(String code) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudyProgramme_.code), code)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public List<StudyProgramme> listByOrganization(Organization organization, Archived archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudyProgramme> criteria = criteriaBuilder.createQuery(StudyProgramme.class);
    Root<StudyProgramme> root = criteria.from(StudyProgramme.class);
    criteria.select(root);
    
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get(StudyProgramme_.organization), organization));
    
    if (archived.isBoolean()) {
      predicates.add(criteriaBuilder.equal(root.get(StudyProgramme_.archived), archived.booleanValue()));
    }
    
    criteria.where(
        predicates.toArray(new Predicate[0])
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudyProgramme update(StudyProgramme studyProgramme, Organization organization, String name, 
      StudyProgrammeCategory category, String code, String officialEducationType, boolean hasEvaluationFees) {
    studyProgramme.setOrganization(organization);
    studyProgramme.setName(name);
    studyProgramme.setCategory(category);
    studyProgramme.setCode(code);
    studyProgramme.setOfficialEducationType(officialEducationType);
    studyProgramme.setHasEvaluationFees(hasEvaluationFees);
    
    studyProgrammeUpdatedEvent.fire(new StudyProgrammeUpdatedEvent(studyProgramme.getId()));
    
    return persist(studyProgramme);
  }

  /**
   * Persists StudyProgramme after properties have changed.
   * 
   * This is just a dummy method to persist a StudyProgramme as
   * the properties need to be changed outside this DAO.
   * 
   * @param studyProgramme
   * @return
   */
  public StudyProgramme updateProperties(StudyProgramme studyProgramme) {
    studyProgrammeUpdatedEvent.fire(new StudyProgrammeUpdatedEvent(studyProgramme.getId()));
    return persist(studyProgramme);
  }
  
  @Override
  public void archive(ArchivableEntity entity, User modifier) {
    super.archive(entity, modifier);
    if (entity instanceof StudyProgramme) {
      StudyProgramme studyProgramme = (StudyProgramme) entity;
      studyProgrammeRemovedEvent.fire(new StudyProgrammeArchivedEvent(studyProgramme.getId()));
    }
  }

}
