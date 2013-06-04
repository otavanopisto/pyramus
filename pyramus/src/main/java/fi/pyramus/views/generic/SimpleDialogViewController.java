package fi.pyramus.views.generic;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class SimpleDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    String nonlocalizedMessage = requestContext.getString("message");
    if (nonlocalizedMessage == null) {
      String localeId = requestContext.getString("localeId");
      String localeParamsParameter = requestContext.getString("localeParams");
      String[] localeParams = null;
      
      if (!StringUtils.isBlank(localeParamsParameter)) {
        localeParams = localeParamsParameter.split(",");
      }
      
      String message = Messages.getInstance().getText(requestContext.getRequest().getLocale(), localeId, localeParams);
      
      requestContext.getRequest().setAttribute("message", message);
      requestContext.setIncludeJSP("/templates/generic/simpledialog.jsp");
    } else {
      requestContext.getRequest().setAttribute("message", nonlocalizedMessage);
      requestContext.setIncludeJSP("/templates/generic/simpledialog.jsp");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
