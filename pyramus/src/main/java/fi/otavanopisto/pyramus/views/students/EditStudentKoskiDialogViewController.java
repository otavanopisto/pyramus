package fi.otavanopisto.pyramus.views.students;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.PersonVariableDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.json.students.ListKoskiPersonVariablesJSONRequestController;
import fi.otavanopisto.pyramus.koski.KoskiConsts;

public class EditStudentKoskiDialogViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(ListKoskiPersonVariablesJSONRequestController.class.getName());

  public void process(PageRequestContext requestContext) {
    PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
    PersonVariableDAO personVariableDAO = DAOFactory.getInstance().getPersonVariableDAO();

    Long personId = requestContext.getLong("personId");
    
    if (personId == null) {
      logger.log(Level.WARNING, "Unable to load log entries due to missing personId.");
      try {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Error loading log entries", e);
      }
      return;
    }
    
    Person person = personDAO.findById(personId);
    String personOid = personVariableDAO.findByPersonAndKey(person, KoskiConsts.VariableNames.KOSKI_HENKILO_OID);
    
    requestContext.getRequest().setAttribute("person", person);
    requestContext.getRequest().setAttribute("personOID", personOid);
    
    requestContext.setIncludeJSP("/templates/students/editstudentkoskidialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
