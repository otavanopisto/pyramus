package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

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

public class ListKoskiPersonVariablesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListKoskiPersonVariablesJSONRequestController.class.getName());
  
  public void process(JSONRequestContext requestContext) {
    try {
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
      PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
      UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
      
      Long personId = requestContext.getLong("personId");
      
      if (personId == null) {
        logger.log(Level.WARNING, "Unable to load log entries due to missing personId.");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      Person person = personDAO.findById(personId);
      String personOid = personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID);
      
      List<Map<String, Object>> studentVariables = new ArrayList<>();
      List<Student> students = studentDAO.listByPerson(person);
      students.sort((a, b) -> Comparator.nullsFirst(Date::compareTo).reversed().compare(a.getStudyStartDate(), b.getStudyStartDate()));
      for (Student student : students) {
        Map<String, Object> studentInfo = new HashMap<>();
        studentInfo.put("studentId", student.getId());
        studentInfo.put("studyProgrammeName", student.getStudyProgramme().getName());
        studentInfo.put("oid", userVariableDAO.findByUserAndKey(student, KoskiConsts.VariableNames.KOSKI_STUDYPERMISSION_ID));
        studentInfo.put("linkedOid", userVariableDAO.findByUserAndKey(student, KoskiConsts.VariableNames.KOSKI_LINKED_STUDYPERMISSION_ID));
        studentInfo.put("studyStartDate", student.getStudyStartDate().getTime());
        studentVariables.add(studentInfo);
      }
      
      requestContext.addResponseParameter("personOID", personOid);
      requestContext.addResponseParameter("studentVariables", studentVariables);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading log entries", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
