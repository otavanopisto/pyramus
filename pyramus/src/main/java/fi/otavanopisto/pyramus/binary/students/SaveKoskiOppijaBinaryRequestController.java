package fi.otavanopisto.pyramus.binary.students;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.BinaryRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.framework.BinaryRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.koski.KoskiController;
import fi.otavanopisto.pyramus.koski.model.Oppija;

public class SaveKoskiOppijaBinaryRequestController extends BinaryRequestController {

  private static final Logger logger = Logger.getLogger(SaveKoskiOppijaBinaryRequestController.class.getName());
  
  public void process(BinaryRequestContext requestContext) {
    try {
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      
      Long personId = requestContext.getLong("personId");
      
      if (personId == null) {
        logger.log(Level.WARNING, "Missing personId.");
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      Person person = personDAO.findById(personId);
      
      KoskiController koskiController = CDI.current().select(KoskiController.class).get();

      Oppija oppija = koskiController.personToOppija(person);
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      String requestStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(oppija);
      
      requestContext.setResponseContent(requestStr.getBytes("UTF-8"), "application/json;charset=UTF-8");
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading person variables", e);
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
