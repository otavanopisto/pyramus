package fi.pyramus.views.resources;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.resources.ResourceCategoryDAO;
import fi.pyramus.dao.resources.ResourceDAO;
import fi.pyramus.dao.resources.WorkResourceDAO;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.resources.Resource;
import fi.pyramus.domainmodel.resources.ResourceCategory;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

public class EditWorkResourceViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext pageRequestContext) {
    ResourceDAO resourceDAO = DAOFactory.getInstance().getResourceDAO();
    WorkResourceDAO workResourceDAO = DAOFactory.getInstance().getWorkResourceDAO();
    ResourceCategoryDAO resourceCategoryDAO = DAOFactory.getInstance().getResourceCategoryDAO();

    Long resourceId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("resource"));
    Resource resource = resourceDAO.findById(resourceId);

    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = resource.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<ResourceCategory> resourceCategories = resourceCategoryDAO.listUnarchived();
    Collections.sort(resourceCategories, new StringAttributeComparator("getName"));

    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("categories", resourceCategories);
    pageRequestContext.getRequest().setAttribute("resource", workResourceDAO.findById(resource.getId()));
    pageRequestContext.setIncludeJSP("/templates/resources/editworkresource.jsp");
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
    return Messages.getInstance().getText(locale, "resources.editWorkResource.pageTitle");
  }

}
