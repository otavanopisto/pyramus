package fi.otavanopisto.pyramus.dao.auditlog;

import javax.ejb.Stateless;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.audit.AuditLog;

@Stateless
public class AuditLogDAO extends PyramusEntityDAO<AuditLog> {

}