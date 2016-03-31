package fi.otavanopisto.pyramus.views.help;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.help.HelpPageDAO;
import fi.otavanopisto.pyramus.domainmodel.help.HelpItemTitle;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPage;
import fi.otavanopisto.pyramus.domainmodel.help.HelpPageContent;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ViewHelpPageViewController extends PyramusViewController implements Breadcrumbable {

  public void process(PageRequestContext requestContext) {
    HelpPageDAO helpPageDAO = DAOFactory.getInstance().getHelpPageDAO();

    Long pageId = requestContext.getLong("page");
    
    HelpPage helpPage = helpPageDAO.findById(pageId);
    
    HelpItemTitle itemTitle = helpPage.getTitleByLocale(requestContext.getRequest().getLocale());
    if (itemTitle == null)
      itemTitle = helpPage.getTitles().get(0);
    
    HelpPageContent pageContent = helpPage.getContentByLocale(requestContext.getRequest().getLocale());
    if (pageContent == null)
      pageContent = helpPage.getContents().get(0);
    

    requestContext.getRequest().setAttribute("title", itemTitle.getTitle());
    requestContext.getRequest().setAttribute("content", pageContent.getContent());
    
    requestContext.setIncludeJSP("/templates/help/viewhelppage.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "help.viewHelpPage.pageTitle");
  }

}
