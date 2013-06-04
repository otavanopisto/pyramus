package fi.pyramus.views.resources;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

public class CreateWorkResourceViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();
    
    String name = pageRequestContext.getString("name");
    
    List<ResourceCategory> resourceCategories = resourceCategoryDAO.listUnarchived();
    Collections.sort(resourceCategories, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("name", name);
    pageRequestContext.getRequest().setAttribute("categories", resourceCategories);
    pageRequestContext.setIncludeJSP("/templates/resources/createworkresource.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "resources.createWorkResource.pageTitle");
  }

}
