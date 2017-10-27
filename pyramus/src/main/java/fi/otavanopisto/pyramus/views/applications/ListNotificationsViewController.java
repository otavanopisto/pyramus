package fi.otavanopisto.pyramus.views.applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationNotificationDAO;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationNotification;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;

public class ListNotificationsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    ApplicationNotificationDAO applicationNotificationDAO = DAOFactory.getInstance().getApplicationNotificationDAO();
    List<ApplicationNotification> applicationNotifications = applicationNotificationDAO.listAll();
    Collections.sort(applicationNotifications, new Comparator<ApplicationNotification>() {
      public int compare(ApplicationNotification o1, ApplicationNotification o2) {
        String s1 = StringUtils.defaultIfBlank(o1.getLine(), "");
        String s2 = StringUtils.defaultIfBlank(o2.getLine(), "");
        return s1.equals(s2) ? o1.getState().ordinal() - o2.getState().ordinal() : s1.compareTo(s2);
      }
    });
    List<Map<String, Object>> results = new ArrayList<>();
    for (ApplicationNotification applicationNotification : applicationNotifications) {
      Map<String, Object> notification = new HashMap<>();
      notification.put("id", applicationNotification.getId());
      notification.put("line", StringUtils.isBlank(applicationNotification.getLine()) ? "Kaikki linjat" : ApplicationUtils.applicationLineUiValue(applicationNotification.getLine()));
      notification.put("state", ApplicationUtils.applicationStateUiValue(applicationNotification.getState()));
      notification.put("userCount", applicationNotification.getUsers().size());
      results.add(notification);
    }
    String jsonNotifications = JSONArray.fromObject(results).toString();
    setJsDataVariable(pageRequestContext, "notifications", jsonNotifications);
    pageRequestContext.setIncludeJSP("/templates/applications/listnotifications.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER };
  }

}
