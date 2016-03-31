package fi.otavanopisto.pyramus.dao.reports;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContext;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.pyramus.domainmodel.reports.ReportContext_;

@Stateless
public class ReportDAO extends PyramusEntityDAO<Report> {

  public Report create(String name, String data, User creatingUser) {
    Date now = new Date(System.currentTimeMillis());
    
    Report report = new Report();
    
    report.setData(data);
    report.setName(name);
    report.setCreated(now);
    report.setCreator(creatingUser);
    report.setLastModified(now);
    report.setLastModifier(creatingUser);

    getEntityManager().persist(report);

    return report;
  }
  
  public void update(Report report, String name, ReportCategory reportCategory) {
    EntityManager entityManager = getEntityManager();

    report.setName(name);
    report.setCategory(reportCategory);
    
    entityManager.persist(report);
  }

  public void updateName(Report report, String name, User modifyingUser) {
    Date now = new Date(System.currentTimeMillis());
    
    report.setName(name);
    report.setLastModified(now);
    report.setLastModifier(modifyingUser);
    
    getEntityManager().persist(report);
  }

  public void updateData(Report report, String data, User modifyingUser) {
    Date now = new Date(System.currentTimeMillis());
    
    report.setData(data);
    report.setLastModified(now);
    report.setLastModifier(modifyingUser);
    
    getEntityManager().persist(report);
  }

  public List<Report> listByContextType(ReportContextType contextType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Report> criteria = criteriaBuilder.createQuery(Report.class);
    Root<ReportContext> root = criteria.from(ReportContext.class);
    
    criteria.select(root.get(ReportContext_.report));
    criteria.where(
        criteriaBuilder.equal(root.get(ReportContext_.context), contextType)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  
  @Override
  public void delete(Report report) {
    super.delete(report);
  }
  
}