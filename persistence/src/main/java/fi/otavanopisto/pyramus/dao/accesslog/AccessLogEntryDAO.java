package fi.otavanopisto.pyramus.dao.accesslog;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.accesslog.AccessLogEntry;
import fi.otavanopisto.pyramus.domainmodel.accesslog.AccessLogEntryPath;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Stateless
public class AccessLogEntryDAO extends PyramusEntityDAO<AccessLogEntry> {

  public AccessLogEntry create(User user, String ip, Date date, AccessLogEntryPath path, String parameters) {
    EntityManager entityManager = getEntityManager();

    AccessLogEntry accessLogEntry = new AccessLogEntry();
    
    accessLogEntry.setUser(user);
    accessLogEntry.setIp(ip);
    accessLogEntry.setDate(date);
    accessLogEntry.setPath(path);
    accessLogEntry.setParameters(parameters);
    
    entityManager.persist(accessLogEntry);

    return accessLogEntry;
  }

}
