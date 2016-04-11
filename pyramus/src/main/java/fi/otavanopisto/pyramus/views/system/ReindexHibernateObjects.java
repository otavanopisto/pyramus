package fi.otavanopisto.pyramus.views.system;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.logging.Logging;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.SystemDAO;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ReindexHibernateObjects extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    boolean redirectBack = false;
    
    Class<?> indexClass = null;
    String indexClassName = requestContext.getString("class");
    if (StringUtils.isBlank(indexClassName)) {
      HttpSession session = requestContext.getRequest().getSession();
      @SuppressWarnings("unchecked")
      List<Class<?>> pendingIndexingTasks = (List<Class<?>>) session.getAttribute("pendingIndexingTasks");
      
      if (pendingIndexingTasks == null) {
        pendingIndexingTasks = systemDAO.getIndexedEntities();
        session.setAttribute("pendingIndexingTasks", pendingIndexingTasks);
        redirectBack = true;
      } else {
        if (!pendingIndexingTasks.isEmpty()) {
          indexClass = pendingIndexingTasks.get(0);
          pendingIndexingTasks.remove(0);
          session.setAttribute("pendingIndexingTasks", pendingIndexingTasks);
          redirectBack = true;
        } else {
          session.removeAttribute("pendingIndexingTasks");
        }
      }
    } else {
      try {
        indexClass = Class.forName(indexClassName);
      } catch (ClassNotFoundException e) {
      }
    }
   
    if (indexClass != null) {
      Logging.logInfo("Indexing class " + indexClass.toString());
  
      try {
        systemDAO.reindexHibernateSearchObjects(indexClass, 200);
      } catch (InterruptedException e) {
        throw new SmvcRuntimeException(e);
      }
    }
    
    if (redirectBack) {
      if (indexClass != null) {
        requestContext.getRequest().setAttribute("entityName", indexClass.toString());
      }
      
      requestContext.setIncludeJSP("/templates/system/reindexhibernateobjects.jsp");
    } else {
      requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/index.page");
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
