package fi.otavanopisto.pyramus.dao.grading;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment_;
import fi.otavanopisto.pyramus.domainmodel.grading.Credit;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExam;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExamSkillLevel;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExam_;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;

@Stateless
public class SpokenLanguageExamDAO extends PyramusEntityDAO<SpokenLanguageExam> {

  public SpokenLanguageExam create(Credit credit, Grade grade, SpokenLanguageExamSkillLevel skillLevel, String verbalAssessment, LocalDateTime timestamp, StaffMember assessor) {
    SpokenLanguageExam entity = new SpokenLanguageExam();

    entity.setCredit(credit);
    entity.setGrade(grade);
    entity.setSkillLevel(skillLevel);
    entity.setVerbalAssessment(verbalAssessment);
    entity.setTimestamp(timestamp);
    entity.setAssessor(assessor);
    
    return persist(entity);
  }

  public SpokenLanguageExam findBy(Credit credit) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SpokenLanguageExam> criteria = criteriaBuilder.createQuery(SpokenLanguageExam.class);
    Root<SpokenLanguageExam> root = criteria.from(SpokenLanguageExam.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(SpokenLanguageExam_.credit), credit)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<SpokenLanguageExam> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SpokenLanguageExam> criteria = criteriaBuilder.createQuery(SpokenLanguageExam.class);
    Root<SpokenLanguageExam> root = criteria.from(SpokenLanguageExam.class);
    
    Subquery<CourseAssessment> courseAssessmentQuery = criteria.subquery(CourseAssessment.class);
    Root<CourseAssessment> courseAssessment = courseAssessmentQuery.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudent = courseAssessment.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> course = courseStudent.join(CourseStudent_.course);
    courseAssessmentQuery.select(courseAssessment);
    courseAssessmentQuery.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.student), student),
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseAssessment.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(course.get(Course_.archived), Boolean.FALSE)
        )
    );
    
    Subquery<TransferCredit> transferCreditQuery = criteria.subquery(TransferCredit.class);
    Root<TransferCredit> transferCredit = transferCreditQuery.from(TransferCredit.class);
    transferCreditQuery.select(transferCredit);
    transferCreditQuery.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(transferCredit.get(TransferCredit_.student), student),
            criteriaBuilder.equal(transferCredit.get(TransferCredit_.archived), Boolean.FALSE)
        )
    );
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.or(
            root.get(SpokenLanguageExam_.credit).in(courseAssessmentQuery),
            root.get(SpokenLanguageExam_.credit).in(transferCreditQuery)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public SpokenLanguageExam update(SpokenLanguageExam spokenLanguageExam, Grade grade, SpokenLanguageExamSkillLevel skillLevel,
      String verbalAssessment, LocalDateTime timestamp, StaffMember assessor) {
    spokenLanguageExam.setGrade(grade);
    spokenLanguageExam.setSkillLevel(skillLevel);
    spokenLanguageExam.setVerbalAssessment(verbalAssessment);
    spokenLanguageExam.setTimestamp(timestamp);
    spokenLanguageExam.setAssessor(assessor);
    return persist(spokenLanguageExam);
  }

}
