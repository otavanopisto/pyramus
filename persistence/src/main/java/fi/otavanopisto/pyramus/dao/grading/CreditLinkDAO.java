package fi.otavanopisto.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import fi.otavanopisto.pyramus.dao.Predicates;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment_;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditLink_;
import fi.otavanopisto.pyramus.domainmodel.grading.CreditType;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit_;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class CreditLinkDAO extends PyramusEntityDAO<CreditLink> {

  public CreditLink create(Credit credit, Student student, User creator) {
    CreditLink creditLink = new CreditLink();
    creditLink.setCredit(credit);
    creditLink.setStudent(student);
    creditLink.setCreator(creator);
    creditLink.setCreated(new Date());
    creditLink.setArchived(Boolean.FALSE);
    
    return persist(creditLink);
  }

  public List<CreditLink> listByStudentAndType(Student student, CreditType creditType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    Join<CreditLink, Credit> join = root.join(CreditLink_.credit);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(join.get(Credit_.creditType), creditType),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CreditLink> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CreditLink> listLinkedCourseAssessments(Student student, Subject subject, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    
    Root<CreditLink> creditLink = criteria.from(CreditLink.class);
    Root<CourseAssessment> courseAssessment = criteria.from(CourseAssessment.class);

    Join<CourseAssessment, CourseStudent> courseStudentJoin = courseAssessment.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);

    Predicates predicates = Predicates.newInstance()
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.student), student))
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.credit), courseAssessment))
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.archived), Boolean.FALSE))
      .add(criteriaBuilder.equal(courseAssessment.get(CourseAssessment_.archived), Boolean.FALSE))
      .add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE))
      .add(criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE));

    if (subject != null) {
      predicates.add(criteriaBuilder.equal(courseJoin.get(Course_.subject), subject));
    }
    
    if (curriculum != null) {
      SetJoin<Course, Curriculum> curriculumJoin = courseJoin.join(Course_.curriculums);
      predicates.add(criteriaBuilder.equal(curriculumJoin, curriculum));
    }
    
    criteria.select(creditLink);
    criteria.where(criteriaBuilder.and(predicates.array()));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CreditLink> listLinkedTransferCredits(Student student, Subject subject, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    
    Root<CreditLink> creditLink = criteria.from(CreditLink.class);
    Root<TransferCredit> transferCredit = criteria.from(TransferCredit.class);

    Predicates predicates = Predicates.newInstance()
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.student), student))
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.credit), transferCredit))
      .add(criteriaBuilder.equal(creditLink.get(CreditLink_.archived), Boolean.FALSE))
      .add(criteriaBuilder.equal(transferCredit.get(TransferCredit_.archived), Boolean.FALSE));

    if (subject != null) {
      predicates.add(criteriaBuilder.equal(transferCredit.get(TransferCredit_.subject), subject));
    }
    
    if (curriculum != null) {
      predicates.add(criteriaBuilder.equal(transferCredit.get(TransferCredit_.curriculum), curriculum));
    }
      
    criteria.select(creditLink);
    criteria.where(criteriaBuilder.and(predicates.array()));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public Long countByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public CreditLink findByStudentAndCredit(Student student, Credit credit) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CreditLink> criteria = criteriaBuilder.createQuery(CreditLink.class);
    Root<CreditLink> root = criteria.from(CreditLink.class);
    
    // TODO Credit archived?
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CreditLink_.student), student),
            criteriaBuilder.equal(root.get(CreditLink_.credit), credit),
            criteriaBuilder.equal(root.get(CreditLink_.archived), Boolean.FALSE)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

}
