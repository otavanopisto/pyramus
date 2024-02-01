package fi.otavanopisto.pyramus.views.studentparents;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.ContactTypeDAO;
import fi.otavanopisto.pyramus.dao.base.ContactURLTypeDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.dao.users.StudentParentDAO;
import fi.otavanopisto.pyramus.dao.users.UserIdentificationDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableDAO;
import fi.otavanopisto.pyramus.dao.users.UserVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.UserIdentification;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariable;
import fi.otavanopisto.pyramus.domainmodel.users.UserVariableKey;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.plugin.auth.AuthenticationProviderVault;
import fi.otavanopisto.pyramus.plugin.auth.InternalAuthenticationProvider;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Edit User view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.users.EditUserJSONRequestController
 */
public class EditStudentParentViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    // TODO loggedUserRole vs. user role
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentParentDAO studentParentDAO = DAOFactory.getInstance().getStudentParentDAO();
    UserVariableDAO userVariableDAO = DAOFactory.getInstance().getUserVariableDAO();
    UserVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getUserVariableKeyDAO();
    ContactTypeDAO contactTypeDAO = DAOFactory.getInstance().getContactTypeDAO();
    ContactURLTypeDAO contactURLTypeDAO = DAOFactory.getInstance().getContactURLTypeDAO();
    UserIdentificationDAO userIdentificationDAO = DAOFactory.getInstance().getUserIdentificationDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();

    StaffMember loggedUser = staffDAO.findById(pageRequestContext.getLoggedUserId());
    
    StudentParent user = studentParentDAO.findById(pageRequestContext.getLong("userId"));
    
    if (!UserUtils.canAccessOrganization(loggedUser, user.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Cannot access users' organization.");
    }
    
    String username = "";
    boolean hasInternalAuthenticationStrategies = AuthenticationProviderVault.getInstance().hasInternalStrategies();
    if (hasInternalAuthenticationStrategies) {
      // TODO: Support for multiple internal authentication providers
      List<InternalAuthenticationProvider> internalAuthenticationProviders = AuthenticationProviderVault.getInstance().getInternalAuthenticationProviders();
      if (internalAuthenticationProviders.size() == 1) {
        InternalAuthenticationProvider internalAuthenticationProvider = internalAuthenticationProviders.get(0);
        if (internalAuthenticationProvider != null) {
          UserIdentification userIdentification = userIdentificationDAO.findByAuthSourceAndPerson(internalAuthenticationProvider.getName(), user.getPerson());
          if (internalAuthenticationProvider.canUpdateCredentials()) {
            if (userIdentification != null) {
              username = internalAuthenticationProvider.getUsername(userIdentification.getExternalId());
            }
          }
        }
      }
    }

    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = user.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }

    List<ContactURLType> contactURLTypes = contactURLTypeDAO.listUnarchived();
    Collections.sort(contactURLTypes, new StringAttributeComparator("getName"));

    List<ContactType> contactTypes = contactTypeDAO.listUnarchived();
    Collections.sort(contactTypes, new StringAttributeComparator("getName"));

    List<UserVariableKey> userVariableKeys = variableKeyDAO.listUserEditableUserVariableKeys();
    Collections.sort(userVariableKeys, new StringAttributeComparator("getVariableName"));
    
    JSONArray variables = new JSONArray();
    for (UserVariableKey userVariableKey : userVariableKeys) {
      UserVariable userVariable = userVariableDAO.findByUserAndVariableKey(user, userVariableKey);
      JSONObject variable = new JSONObject();
      variable.put("type", userVariableKey.getVariableType());
      variable.put("name", userVariableKey.getVariableName());
      variable.put("key", userVariableKey.getVariableKey());
      variable.put("value", userVariable != null ? userVariable.getValue() : "");
      variables.add(variable);
    }
    setJsDataVariable(pageRequestContext, "variables", variables.toString());

    List<Organization> organizations;
    if (UserUtils.canAccessAllOrganizations(loggedUser)) {
      organizations = organizationDAO.listUnarchived();
    } else {
      organizations = Arrays.asList(loggedUser.getOrganization());
    }
    Collections.sort(organizations, new StringAttributeComparator("getName"));
    
    // Audit (not logging when returning to this page after save reloads it) 

    if (!StringUtils.contains(pageRequestContext.getReferer(false), "editstudentparent.page")) {
      studentParentDAO.auditView(user.getPersonId(), user.getId(), "Edit Student Parent");
    }
    
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    Collections.sort(studyProgrammes, Comparator.comparing(StudyProgramme::getName));
    
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("user", user);
    pageRequestContext.getRequest().setAttribute("hasInternalAuthenticationStrategies", hasInternalAuthenticationStrategies);
    pageRequestContext.getRequest().setAttribute("username", username);
    pageRequestContext.getRequest().setAttribute("contactTypes", contactTypes);
    pageRequestContext.getRequest().setAttribute("contactURLTypes", contactURLTypes);
    pageRequestContext.getRequest().setAttribute("organizations", organizations);
    pageRequestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    
    pageRequestContext.setIncludeJSP("/templates/studentparents/editstudentparent.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Available for only those
   * with {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "studentparents.editStudentParent.pageTitle");
  }

}
