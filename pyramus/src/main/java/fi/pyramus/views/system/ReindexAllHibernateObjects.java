package fi.pyramus.views.system;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.AccessDeniedException;
import fi.internetix.smvc.LoginRequiredException;
import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.internetix.smvc.controllers.RequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class ReindexAllHibernateObjects extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();
    for (Class<?> entity : systemDAO.getIndexedEntities()) {
      try {
        systemDAO.reindexHibernateSearchObjects(entity, 200);
      } catch (InterruptedException e) {
        throw new SmvcRuntimeException(StatusCode.UNDEFINED, "Reindexing interrupted");
      }
    }
  }
  
  @Override
  public void authorize(RequestContext requestContext) throws LoginRequiredException, AccessDeniedException {
    String systemKey = System.getProperty("pyramus-reindex-key");
    if (StringUtils.isNotBlank(systemKey)) {
      String authorizationHeader = requestContext.getRequest().getHeader("Authorization");
      
      if (StringUtils.startsWith(authorizationHeader, "ReindexKey ")) {
        String headerKey = StringUtils.substring(authorizationHeader, 11);
        if (StringUtils.equals(headerKey, systemKey)) {
          return;
        }
      }
    }
    
    throw new AccessDeniedException(requestContext.getRequest().getLocale());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
