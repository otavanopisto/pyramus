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
import fi.pyramus.domainmodel.resources.ResourceType;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

public class SearchResourcesViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();

    List<ResourceCategory> resourceCategories = resourceCategoryDAO.listUnarchived();
    Collections.sort(resourceCategories, new StringAttributeComparator("getName"));

    requestContext.getRequest().setAttribute("resourceCategories", resourceCategories);
    requestContext.getRequest().setAttribute("resourceTypes", ResourceType.values());
    requestContext.setIncludeJSP("/templates/resources/searchresources.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "resources.searchResources.pageTitle");
  }
  
}
