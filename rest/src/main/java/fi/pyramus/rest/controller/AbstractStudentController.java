package fi.pyramus.rest.controller;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;

@Dependent
@Stateless
public class AbstractStudentController {
  @Inject
  AbstractStudentDAO abstractStudentDAO;

  public AbstractStudent createAbstractStudent(Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    AbstractStudent abstractStudent = abstractStudentDAO.create(birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return abstractStudent;
  }
  
  public List<AbstractStudent> findAbstractStudents() {
    List<AbstractStudent> abstractStudents = abstractStudentDAO.listAll();
    return abstractStudents;
  }
  
  public List<AbstractStudent> findUnarchivedAbstractStudents() {
    List<AbstractStudent> abstractStudents = abstractStudentDAO.listUnarchived();
    return abstractStudents;
  }
  
  public AbstractStudent findAbstractStudentById(Long id) {
    AbstractStudent abstractStudent = abstractStudentDAO.findById(id);
    return abstractStudent;
  }
  
  public AbstractStudent updateAbstractStudent(AbstractStudent abstractStudent, Date birthday, String socialSecurityNumber, Sex sex, String basicInfo, Boolean secureInfo) {
    abstractStudentDAO.update(abstractStudent, birthday, socialSecurityNumber, sex, basicInfo, secureInfo);
    return abstractStudent;
  }
}
