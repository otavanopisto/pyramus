package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.AbstractStudentDAO;
import fi.pyramus.domainmodel.students.AbstractStudent;
import fi.pyramus.domainmodel.students.Sex;

public class AbstractStudentAPI {

  public AbstractStudentAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(Date birthday, String socialSecurityNumber, String sex, String basicInfo, boolean secureInfo) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    Sex sexEntity;

    if ("m".equals(sex)) {
      sexEntity = Sex.MALE;
    } else if ("f".equals(sex)) {
      sexEntity = Sex.FEMALE;
    } else {
      throw new IllegalArgumentException("sex must be \"m\" or \"f\"");
    }

    AbstractStudent abstractStudent = abstractStudentDAO.create(birthday, socialSecurityNumber, sexEntity, basicInfo, secureInfo);

    return (abstractStudent.getId());
  }
  
  public Long findIdBySocialSecurityNumber(String socialSecurityNumber) {
    AbstractStudentDAO abstractStudentDAO = DAOFactory.getInstance().getAbstractStudentDAO();
    AbstractStudent abstractStudent = abstractStudentDAO.findBySSN(socialSecurityNumber);
    return abstractStudent != null ? abstractStudent.getId() : null;
  }

  @SuppressWarnings("unused")
  private Long loggedUserId;
}
