package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Edit School view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.EditSchoolJSONRequestController
 */
public class EditSchoolViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();

    Long schoolId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("school"));
    School school = schoolDAO.findById(schoolId);
    
    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = school.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<SchoolVariableKey> schoolUserEditableVariableKeys = schoolVariableKeyDAO.listUserEditableVariableKeys();
    Collections.sort(schoolUserEditableVariableKeys, new StringAttributeComparator("getVariableName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));
    
    String jsonContactTypes = new JSONArrayExtractor("id", "name").extractString(contactTypes);
    
    List<Address> addresses = school.getContactInfo().getAddresses();
    JSONArray jsonAddresses = new JSONArrayExtractor("id",
    												"defaultAddress",
    												"name",
    												"streetAddress",
    												"postalCode",
    												"city",
                                                   	"country").extract(addresses);
    for (int i=0; i<jsonAddresses.size(); i++) {
      JSONObject jsonAddress = jsonAddresses.getJSONObject(i);
      if (addresses.get(i).getContactType() != null) {
        jsonAddress.put("contactTypeId", addresses.get(i).getContactType().getId());
      }
    }
    
    List<Email> emails = school.getContactInfo().getEmails();
    JSONArray jsonEmails = new JSONArrayExtractor("id", "defaultAddress", "address").extract(emails);
    for (int i=0; i<jsonEmails.size(); i++) {
      JSONObject jsonEmail = jsonEmails.getJSONObject(i);
      if (emails.get(i).getContactType() != null) {
        jsonEmail.put("contactTypeId", emails.get(i).getContactType().getId());
      }
    }
    
    List<PhoneNumber> phoneNumbers = school.getContactInfo().getPhoneNumbers();
    JSONArray jsonPhoneNumbers = new JSONArrayExtractor("id", "defaultNumber", "number").extract(phoneNumbers);
    for (int i=0; i<jsonPhoneNumbers.size(); i++) {
      JSONObject jsonPhoneNumber = jsonPhoneNumbers.getJSONObject(i);
      if (phoneNumbers.get(i).getContactType() != null) {
        jsonPhoneNumber.put("contactTypeId", emails.get(i).getContactType().getId());
      }
    }
    
    JSONArray jsonVariableKeys = new JSONArrayExtractor("variableKey", "variableName", "variableType").extract(schoolUserEditableVariableKeys);
    for (int i = 0; i < jsonVariableKeys.size(); i++) {
      JSONObject jsonVariableKey = jsonVariableKeys.getJSONObject(i);
      
      String key = jsonVariableKey.getString("variableKey");
      String value = schoolVariableDAO.findValueBySchoolAndKey(school, key);
      
      if (value != null)
        jsonVariableKey.put("variableValue", value);
    }
    
    this.setJsDataVariable(pageRequestContext, "addresses", jsonAddresses.toString());
    this.setJsDataVariable(pageRequestContext, "emails", jsonEmails.toString());
    this.setJsDataVariable(pageRequestContext, "phoneNumbers", jsonPhoneNumbers.toString());
    this.setJsDataVariable(pageRequestContext, "contactTypes", jsonContactTypes);
    this.setJsDataVariable(pageRequestContext, "variableKeys", jsonVariableKeys.toString());

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString()); 
    pageRequestContext.getRequest().setAttribute("school", school);
    pageRequestContext.getRequest().setAttribute("variableKeys", schoolUserEditableVariableKeys);
    pageRequestContext.getRequest().setAttribute("schoolFields", schoolFieldDAO.listUnarchived());

    pageRequestContext.setIncludeJSP("/templates/settings/editschool.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.editSchool.pageTitle");
  }

}
