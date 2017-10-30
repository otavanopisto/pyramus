package fi.otavanopisto.pyramus.koski;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Stateless
public class KoskiUpdater {

  @Inject
  private KoskiClient koskiClient;
  
  @Inject
  private StudentDAO studentDAO;
  
  @Asynchronous
  public void updateStudent(Long studentId) {
    Student student = studentDAO.findById(studentId);
    try {
      koskiClient.updateStudent(student);
    } catch (KoskiException e) {
    }
  }
  
}
