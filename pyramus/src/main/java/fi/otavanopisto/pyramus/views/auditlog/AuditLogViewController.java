package fi.otavanopisto.pyramus.views.auditlog;

import java.util.Comparator;
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
    AuditLogDAO auditLogDAO = DAOFactory.getInstance().getAuditLogDAO();
    List<AuditLog> entries = auditLogDAO.listAll();
    entries.sort(Comparator.comparing(AuditLog::getDate).reversed());
    requestContext.getRequest().setAttribute("entries", entries);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}