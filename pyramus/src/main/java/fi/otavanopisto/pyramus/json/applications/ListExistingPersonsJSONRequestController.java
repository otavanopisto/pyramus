package fi.otavanopisto.pyramus.json.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.base.EmailDAO;
import fi.otavanopisto.pyramus.dao.base.PersonDAO;
import fi.otavanopisto.pyramus.dao.users.UserDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.views.applications.ApplicationUtils;
import net.sf.json.JSONObject;

public class ListExistingPersonsJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(ListExistingPersonsJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    try {
      Long applicationEntityId = Long.valueOf(requestContext.getRequest().getParameter("applicationEntityId"));
      ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();
      Application application = applicationDAO.findById(applicationEntityId);
      if (application == null) {
        requestContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      
      JSONObject applicationData = JSONObject.fromObject(application.getFormData());      
      
      boolean hasSsn = applicationData.getString("field-ssn-end") != null;
      String ssn = hasSsn ? ApplicationUtils.constructSSN(applicationData.getString("field-birthday"), applicationData.getString("field-ssn-end")) : null;
      String emailAddress = StringUtils.lowerCase(StringUtils.trim(applicationData.getString("field-email")));
  
      EmailDAO emailDAO = DAOFactory.getInstance().getEmailDAO();
      PersonDAO personDAO = DAOFactory.getInstance().getPersonDAO();
      UserDAO userDAO = DAOFactory.getInstance().getUserDAO(); 
      
      Map<Long, Person> existingPersons = new HashMap<>();
      
      // Persons with email
      
      List<Email> emails = emailDAO.listByAddressLowercase(emailAddress);
      for (Email email : emails) {
        if (email.getContactType() != null && Boolean.FALSE.equals(email.getContactType().getNonUnique())) {
          User user = userDAO.findByContactInfo(email.getContactInfo());
          if (user != null) {
            Person person = user.getPerson();
            if (person != null) {
              existingPersons.put(person.getId(), person);
            }
          }
        }
      }
      
      // Persons with SSN
      
      if (hasSsn) {
        List<Person> persons = personDAO.listBySSNUppercase(ssn);
        for (Person person : persons) {
          existingPersons.put(person.getId(), person);
        }
        
        // Persons with SSN ("wrong" delimiter)
        
        char[] ssnChars = ssn.toCharArray();
        ssnChars[6] = ssnChars[6] == 'A' ? '-' : 'A';
        ssn = ssnChars.toString();
        persons = personDAO.listBySSNUppercase(ssn);
        for (Person person : persons) {
          existingPersons.put(person.getId(), person);
        }
      }
      
      List<Map<String, Object>> results = new ArrayList<>();
      if (!existingPersons.isEmpty()) {
        for (Person person : existingPersons.values()) {
          Map<String, Object> personInfo = new HashMap<>();
          personInfo.put("id", person.getId());
          personInfo.put("name", person.getDefaultUser() == null ? "???" : person.getDefaultUser().getFullName());
          results.add(personInfo);
        }
      }
      requestContext.addResponseParameter("persons", results);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error loading existing students", e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}