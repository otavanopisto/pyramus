package fi.otavanopisto.pyramus.json.students;

import javax.enterprise.inject.spi.CDI;

import org.apache.commons.lang.StringUtils;

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
import fi.otavanopisto.pyramus.koski.KoskiController;

public class EditKoskiPersonVariablesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();

    Long personId = requestContext.getLong("personId");
    Person person = personDAO.findById(personId);

    boolean changed = false;
    String personOid = StringUtils.trim(requestContext.getString("personOid"));
    
    String oldPersonOid = personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID);
    if (!StringUtils.equals(personOid, oldPersonOid)) {
      personVariableDAO.setPersonVariable(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID, personOid);
      changed = true;
    }
    
    Integer personVariableCount = requestContext.getInteger("studentKoskiIDsTable.rowCount");
    if (personVariableCount != null) {
      for (int i = 0; i < personVariableCount; i++) {
        String colPrefix = "studentKoskiIDsTable." + i;
        String linkedOid = StringUtils.trim(requestContext.getString(colPrefix + ".linkedOid"));
        Long studentId = requestContext.getLong(colPrefix + ".studentId");
        Student student = studentDAO.findById(studentId);
        
        if (person.getId().equals(student.getPerson().getId())) {
          String oldLinkedOid = userVariableDAO.findByUserAndKey(student, KoskiConsts.VariableNames.KOSKI_LINKED_STUDYPERMISSION_ID);
          
          if (!StringUtils.equals(linkedOid, oldLinkedOid)) {
            userVariableDAO.setUserVariable(student, KoskiConsts.VariableNames.KOSKI_LINKED_STUDYPERMISSION_ID, linkedOid);
            changed = true;
          }
        }
      }
    }
    
    if (changed) {
      // If any OIDs were changed, put the Person in the update queue
      KoskiController koskiController = CDI.current().select(KoskiController.class).get();
      koskiController.markForUpdate(person);

      // Some of the OIDs are searchable so reindex the entity
      personDAO.forceReindex(person);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}