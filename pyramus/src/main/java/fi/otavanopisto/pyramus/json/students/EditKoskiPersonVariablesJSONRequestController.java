package fi.otavanopisto.pyramus.json.students;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiConsts;

public class EditKoskiPersonVariablesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();

    Long personId = NumberUtils.createLong(requestContext.getRequest().getParameter("personId"));
    Person person = personDAO.findById(personId);

    String personOid = requestContext.getString("personOid");
    personVariableDAO.setPersonVariable(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID, StringUtils.trim(personOid));
    
    Integer personVariableCount = requestContext.getInteger("studentKoskiIDsTable.rowCount");
    if (personVariableCount != null) {
      for (int i = 0; i < personVariableCount; i++) {
        String colPrefix = "studentKoskiIDsTable." + i;
        String linkedOid = requestContext.getString(colPrefix + ".linkedOid");
        Long studentId = requestContext.getLong(colPrefix + ".studentId");
        Student student = studentDAO.findById(studentId);
        
        if (person.getId().equals(student.getPerson().getId())) {
          userVariableDAO.setUserVariable(student, KoskiConsts.VariableNames.KOSKI_LINKED_STUDYPERMISSION_ID, StringUtils.trim(linkedOid));
        }
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}