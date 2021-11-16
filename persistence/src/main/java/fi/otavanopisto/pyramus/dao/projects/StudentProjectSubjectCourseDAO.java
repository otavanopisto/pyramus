package fi.otavanopisto.pyramus.dao.projects;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectSubjectCourse_;

@Stateless
public class StudentProjectSubjectCourseDAO extends PyramusEntityDAO<StudentProjectSubjectCourse> {

  public StudentProjectSubjectCourse create(StudentProject studentProject, Subject subject, Integer courseNumber,
      AcademicTerm academicTerm, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    StudentProjectSubjectCourse studentProjectSubjectCourse = new StudentProjectSubjectCourse();
    studentProjectSubjectCourse.setSubject(subject);
    studentProjectSubjectCourse.setCourseNumber(courseNumber);
    studentProjectSubjectCourse.setAcademicTerm(academicTerm);
    studentProjectSubjectCourse.setOptionality(optionality);
    entityManager.persist(studentProjectSubjectCourse);

    studentProject.addStudentProjectSubjectCourse(studentProjectSubjectCourse);
    entityManager.persist(studentProject);

    return studentProjectSubjectCourse;
  }

  public void update(StudentProjectSubjectCourse studentProjectSubjectCourse,
      AcademicTerm academicTerm, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    studentProjectSubjectCourse.setAcademicTerm(academicTerm);
    studentProjectSubjectCourse.setOptionality(optionality);

    entityManager.persist(studentProjectSubjectCourse);
  }

  public List<StudentProjectSubjectCourse> listByStudentProject(StudentProject studentProject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentProjectSubjectCourse> criteria = criteriaBuilder.createQuery(StudentProjectSubjectCourse.class);

    Root<StudentProjectSubjectCourse> root = criteria.from(StudentProjectSubjectCourse.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentProjectSubjectCourse_.studentProject), studentProject)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(StudentProjectSubjectCourse studentProjectSubjectCourse) {
    EntityManager entityManager = getEntityManager();
    
    StudentProject studentProject = studentProjectSubjectCourse.getStudentProject();
    if (studentProject != null) {
      studentProject.removeStudentProjectSubjectCourse(studentProjectSubjectCourse);

      entityManager.persist(studentProject);
    }

    super.delete(studentProjectSubjectCourse);
  }

}
