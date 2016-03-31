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
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProjectModule;
import fi.pyramus.domainmodel.projects.StudentProjectModule_;

@Stateless
public class StudentProjectModuleDAO extends PyramusEntityDAO<StudentProjectModule> {

  public StudentProjectModule create(StudentProject studentProject, Module module,
      AcademicTerm academicTerm, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    StudentProjectModule studentProjectModule = new StudentProjectModule();
    studentProjectModule.setModule(module);
    studentProjectModule.setAcademicTerm(academicTerm);
    studentProjectModule.setOptionality(optionality);
    entityManager.persist(studentProjectModule);

    studentProject.addStudentProjectModule(studentProjectModule);
    entityManager.persist(studentProject);

    return studentProjectModule;
  }

  public void update(StudentProjectModule studentProjectModule,
      AcademicTerm academicTerm, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    studentProjectModule.setAcademicTerm(academicTerm);
    studentProjectModule.setOptionality(optionality);

    entityManager.persist(studentProjectModule);
  }

  public List<StudentProjectModule> listByStudentProject(StudentProject studentProject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentProjectModule> criteria = criteriaBuilder.createQuery(StudentProjectModule.class);

    Root<StudentProjectModule> root = criteria.from(StudentProjectModule.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentProjectModule_.studentProject), studentProject)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @Override
  public void delete(StudentProjectModule studentProjectModule) {
    EntityManager entityManager = getEntityManager();
    
    StudentProject studentProject = studentProjectModule.getStudentProject();
    if (studentProject != null) {
      studentProject.removeStudentProjectModule(studentProjectModule);

      entityManager.persist(studentProject);
    }

    super.delete(studentProjectModule);
  }

}
