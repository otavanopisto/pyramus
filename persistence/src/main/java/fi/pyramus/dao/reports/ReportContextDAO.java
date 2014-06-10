package fi.pyramus.dao.reports;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportContext;
import fi.pyramus.domainmodel.reports.ReportContextType;
import fi.pyramus.domainmodel.reports.ReportContext_;

@Stateless
public class ReportContextDAO extends PyramusEntityDAO<ReportContext> {

  public ReportContext create(Report report, ReportContextType context) {
    EntityManager entityManager = getEntityManager();

    ReportContext reportContext = new ReportContext();
    reportContext.setReport(report);
    reportContext.setContext(context);

    entityManager.persist(reportContext);
    return reportContext;
  }

  public ReportContext findByReportAndContextType(Report report, ReportContextType contextType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ReportContext> criteria = criteriaBuilder.createQuery(ReportContext.class);
    Root<ReportContext> root = criteria.from(ReportContext.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(ReportContext_.report), report),
            criteriaBuilder.equal(root.get(ReportContext_.context), contextType)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<ReportContext> listByReport(Report report) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ReportContext> criteria = criteriaBuilder.createQuery(ReportContext.class);
    Root<ReportContext> root = criteria.from(ReportContext.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(ReportContext_.report), report)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(ReportContext reportContext) {
    super.delete(reportContext);
  }
  
}