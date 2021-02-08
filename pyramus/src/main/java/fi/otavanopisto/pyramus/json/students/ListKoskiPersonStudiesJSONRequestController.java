package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiClient;
import fi.otavanopisto.pyramus.koski.KoskiConsts;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.koski.KoskiSettings;
import fi.otavanopisto.pyramus.koski.KoskiStudentHandler;
import fi.otavanopisto.pyramus.koski.KoskiStudentId;
import fi.otavanopisto.pyramus.koski.KoskiStudyProgrammeHandler;
import fi.otavanopisto.pyramus.koski.model.result.OpiskeluoikeusReturnVal;
import fi.otavanopisto.pyramus.koski.model.result.OppijaReturnVal;

public class ListKoskiPersonStudiesJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListKoskiPersonStudiesJSONRequestController.class.getName());
  
  public void process(JSONRequestContext requestContext) {
    try {
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
      PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();
      
      Long personId = requestContext.getLong("personId");
      
      if (personId == null) {
        logger.log(Level.WARNING, "Unable to load log entries due to missing personId.");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      Person person = personDAO.findById(personId);
      String personOid = personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID);

      if (StringUtils.isBlank(personOid)) {
        return;
      }
      
      KoskiClient koskiClient = CDI.current().select(KoskiClient.class).get();
      KoskiSettings koskiSettings = CDI.current().select(KoskiSettings.class).get();
      KoskiController koskiController = CDI.current().select(KoskiController.class).get();
      
      // Find student info from Koski
      OppijaReturnVal koskiStudent = koskiClient.findPersonByOid(personOid);

      List<Map<String, Object>> studyPermitIds = new ArrayList<>();
      
      if (koskiStudent != null) {
        Map<String, Student> oidMap = new HashMap<>();
        List<Student> students = studentDAO.listByPerson(person);
        for (Student student : students) {
          if (student.getStudyProgramme() != null) {
            KoskiStudyProgrammeHandler handlerType = koskiSettings.getStudyProgrammeHandlerType(student.getStudyProgramme().getId());
            KoskiStudentHandler handler = handlerType != null ? koskiController.getStudentHandler(handlerType) : null;
            if (handler != null) {
              Set<KoskiStudentId> ids = handler.listOids(student);
              ids.forEach(id -> oidMap.put(id.getOid(), student));
            }
          }
        }
        
        for (OpiskeluoikeusReturnVal studyPermit : koskiStudent.getOpiskeluoikeudet()) {
          String oid = studyPermit.getOid();
          if (oid != null) {
            Student student = oidMap.get(oid);
            
            Map<String, Object> studyPermitInfo = new HashMap<>();
            studyPermitInfo.put("oid", oid);
            studyPermitInfo.put("linkedStudyProgrammeName", student != null ? student.getStudyProgramme().getName() : null);
            studyPermitInfo.put("linkedStudyProgrammeStartDate", student != null ? student.getStudyStartDate().getTime() : null);
            studyPermitInfo.put("linkedStudentId", student != null ? student.getId() : null);
            studyPermitIds.add(studyPermitInfo);
          }
        }
        
      }

      requestContext.addResponseParameter("studyPermitIDs", studyPermitIds);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading log entries", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
