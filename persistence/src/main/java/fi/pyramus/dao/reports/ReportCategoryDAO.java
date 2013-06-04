package fi.pyramus.dao.reports;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportCategory;
import fi.pyramus.domainmodel.reports.Report_;

@Stateless
public class ReportCategoryDAO extends PyramusEntityDAO<ReportCategory> {

  public ReportCategory create(String name, Integer indexColumn) {
    EntityManager entityManager = getEntityManager();

    ReportCategory reportCategory = new ReportCategory();
    reportCategory.setName(name);
    reportCategory.setIndexColumn(indexColumn);

    entityManager.persist(reportCategory);
    return reportCategory;
  }

  public ReportCategory update(ReportCategory reportCategory, String name, Integer indexColumn) {
    EntityManager entityManager = getEntityManager();

    reportCategory.setName(name);
    reportCategory.setIndexColumn(indexColumn);
    
    entityManager.persist(reportCategory);
    return reportCategory;
  }
  
  @Override
  public void delete(ReportCategory reportCategory) {
    super.delete(reportCategory);
  }
  
  public boolean isReportCategoryInUse(ReportCategory reportCategory) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Report> root = criteria.from(Report.class);
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.equal(root.get(Report_.category), reportCategory)
    );
    
    return entityManager.createQuery(criteria).getSingleResult() > 0;
  }
  
}