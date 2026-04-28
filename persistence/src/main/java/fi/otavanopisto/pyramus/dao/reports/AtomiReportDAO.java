package fi.otavanopisto.pyramus.dao.reports;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.atomi.AtomiReportType;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.AtomiReport;
import fi.otavanopisto.pyramus.domainmodel.reports.AtomiReport_;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;

@Stateless
public class AtomiReportDAO extends PyramusEntityDAO<AtomiReport> {

  public AtomiReport create(AtomiReportType type, Report report) {
    EntityManager entityManager = getEntityManager();

    AtomiReport entity = new AtomiReport();
    entity.setType(type);
    entity.setReport(report);

    entityManager.persist(entity);
    return entity;
  }

  public AtomiReport update(AtomiReport entity, Report report) {
    EntityManager entityManager = getEntityManager();

    entity.setReport(report);
    
    entityManager.persist(entity);
    return entity;
  }

  public AtomiReport findByType(AtomiReportType type) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AtomiReport> criteria = criteriaBuilder.createQuery(AtomiReport.class);
    Root<AtomiReport> root = criteria.from(AtomiReport.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(AtomiReport_.type), type)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}