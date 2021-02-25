package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationContactPersonDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationContractPeriodDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPerson;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContractPeriod;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EditOrganizationViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();
    OrganizationDAO organizationDAO = DAOFactory.getInstance().getOrganizationDAO();
    OrganizationContactPersonDAO organizationContactPersonDAO = DAOFactory.getInstance().getOrganizationContactPersonDAO();
    OrganizationContractPeriodDAO organizationContractPeriodDAO = DAOFactory.getInstance().getOrganizationContractPeriodDAO();
    Long organizationId = requestContext.getLong("organizationId");
    Organization organization = organizationDAO.findById(organizationId);
    
    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));

    List<OrganizationContactPerson> contactPersons = organizationContactPersonDAO.listBy(organization);
    JSONArray contactPersonsJSON = new JSONArray();
    for (OrganizationContactPerson contactPerson : contactPersons) {
      JSONObject contactPersonJSON = new JSONObject();
      contactPersonJSON.put("id", contactPerson.getId());
      contactPersonJSON.put("type", contactPerson.getType());
      contactPersonJSON.put("name", contactPerson.getName());
      contactPersonJSON.put("email", contactPerson.getEmail());
      contactPersonJSON.put("phone", contactPerson.getPhone());
      contactPersonJSON.put("title", contactPerson.getTitle());
      contactPersonsJSON.add(contactPersonJSON);
    }
    setJsDataVariable(requestContext, "contactPersons", contactPersonsJSON.toString());

    List<OrganizationContractPeriod> contractPeriods = organizationContractPeriodDAO.listBy(organization);
    JSONArray contractPeriodsJSON = new JSONArray();
    for (OrganizationContractPeriod contractPeriod : contractPeriods) {
      JSONObject contractPeriodJSON = new JSONObject();
      contractPeriodJSON.put("id", contractPeriod.getId());
      contractPeriodJSON.put("begin", contractPeriod.getBegin() != null ? contractPeriod.getBegin().getTime() : null);
      contractPeriodJSON.put("end", contractPeriod.getEnd() != null ? contractPeriod.getEnd().getTime() : null);
      contractPeriodsJSON.add(contractPeriodJSON);
    }
    setJsDataVariable(requestContext, "contractPeriods", contractPeriodsJSON.toString());

    requestContext.getRequest().setAttribute("organization", organization);
    requestContext.getRequest().setAttribute("educationTypes", educationTypes);
    requestContext.setIncludeJSP("/templates/settings/editorganization.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "settings.editOrganization.pageTitle");
  }

}
