package fi.otavanopisto.pyramus.views.auditlog;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.auditlog.AuditLogDAO;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class AuditLogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/auditlog/auditlog.jsp");
    
    Long authorId = requestContext.getLong("author");
    Long personId = requestContext.getLong("person");
    Long userId = requestContext.getLong("user");
    Integer count = requestContext.getInteger("count");
    if (count == null) {
      count = 100;
    }
    
    AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
    
    auditLogDAO.auditView(null, null, "Audit log");
    
    List<AuditLog> entries = auditLogDAO.listLatestByAuthorAndPersonAndUserAndCount(authorId, personId, userId, count);
    
    requestContext.getRequest().setAttribute("count", entries.size());
    requestContext.getRequest().setAttribute("total", auditLogDAO.count());
    requestContext.getRequest().setAttribute("entries", entries);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}