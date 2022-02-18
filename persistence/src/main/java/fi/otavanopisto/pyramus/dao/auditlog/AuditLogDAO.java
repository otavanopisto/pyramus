package fi.otavanopisto.pyramus.dao.auditlog;

import java.util.Date;

import javax.ejb.Stateless;
import javax.servlet.http.HttpSession;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLogType;
import fi.otavanopisto.pyramus.util.ThreadSessionContainer;

@Stateless
public class AuditLogDAO extends PyramusEntityDAO<AuditLog> {

  public AuditLog create(Long personId, Long userId, AuditLogType type, String className, Long entityId, String field, String data) {
    AuditLog auditLog = new AuditLog();
    
    auditLog.setAuthorId(getLoggedUserId());
    auditLog.setClassName(className);
    auditLog.setData(data);
    auditLog.setDate(new Date());
    auditLog.setEntityId(entityId);
    auditLog.setField(field);
    auditLog.setPersonId(personId);
    auditLog.setType(type);
    auditLog.setUserId(userId);
    
    return persist(auditLog);
  }
 
  private Long getLoggedUserId() {
    HttpSession httpSession = ThreadSessionContainer.getSession();
    if (httpSession != null) {
      return (Long) httpSession.getAttribute("loggedUserId");
    }
    
    return null;
  }
}
