package fi.otavanopisto.pyramus.mailer;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.SettingDAO;
import fi.otavanopisto.pyramus.dao.system.SettingKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Setting;
import fi.otavanopisto.pyramus.domainmodel.system.SettingKey;

public class Mailer {

  public static final String PLAINTEXT = "text/plain; charset=UTF-8";
  public static final String HTML = "text/html; charset=UTF-8";
  
  public static final String MAILER_ENABLED = "applications.mailerEnabled";
  public static final String JNDI_APPLICATION = "java:/mail/pyramus-haku";

  private static final Logger logger = Logger.getLogger(Mailer.class.getName());
  
  public static void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content) {
    sendMail(jndiName, mimeType, from, toSet(to), Collections.emptySet(), subject, content, Collections.emptyList());
  }

  public static void sendMail(String jndiName, String mimeType, String from, String to, String cc, String subject, String content) {
    sendMail(jndiName, mimeType, from, toSet(to), toSet(cc), subject, content, Collections.emptyList());
  }

  public static void sendMail(String jndiName, String mimeType, String from, String to, String cc, String subject, String content, List<MailAttachment> mailAttachments) {
    sendMail(jndiName, mimeType, from, toSet(to), toSet(cc), subject, content, mailAttachments);
  }

  public static void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content, MailAttachment mailAttachment) {
    sendMail(jndiName, mimeType, from, toSet(to), Collections.emptySet(), subject, content, Collections.singletonList(mailAttachment));
  }

  public static void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content, List<MailAttachment> mailAttachments) {
    sendMail(jndiName, mimeType, from, toSet(to), Collections.emptySet(), subject, content, mailAttachments);
  }

  public static void sendMail(String jndiName, String mimeType, String from, Set<String> to, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, Collections.emptySet(), subject, content, Collections.emptyList());
  }
  
  public static void sendMail(String jndiName, String mimeType, String from, Set<String> to, String cc, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, Collections.singleton(cc), subject, content, Collections.emptyList());
  }

  public static void sendMail(String jndiName, String mimeType, String from, Set<String> to, Set<String> cc, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, cc, subject, content, Collections.emptyList());
  }
  
  public static void sendMail(String jndiName, String mimeType, String from, Set<String> to, Set<String> cc, String subject, String content, List<MailAttachment> mailAttachments) {
    MailService mailService = (MailService) findByClass(MailService.class);
    if (mailService == null) {
      logger.log(Level.SEVERE, "MailService not bound");
      return;
    }
    boolean mailEnabled = Boolean.parseBoolean(getSetting(MAILER_ENABLED));
    if (!mailEnabled) {
      logger.info(String.format("Debug mail due to %s not being enabled", jndiName));
      logger.info("From: " + from);
      logger.info("To: " + toCDT(to));
      logger.info("Cc: " + toCDT(cc));
      logger.info("Subject: " + subject);
      logger.info("Content: " + content);
    }
    else {
      mailService.sendMail(jndiName, mimeType, from, to, cc, Collections.emptySet(), subject, content, mailAttachments);
    }
  }

  private static String getSetting(String settingName) {
    SettingDAO settingDAO = DAOFactory.getInstance().getSettingDAO();
    SettingKeyDAO settingKeyDAO = DAOFactory.getInstance().getSettingKeyDAO();
    SettingKey key = settingKeyDAO.findByName(settingName);
    if (key != null) {
      Setting setting = settingDAO.findByKey(key);
      
      if (setting != null) {
        return setting.getValue();
      }
    }
    
    return null;
  }

  private static String toCDT(Set<String> set) {
    if (set == null || set.isEmpty()) {
      return "-";
    }
    StringBuilder sb = new StringBuilder();
    for (String s : set) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(s);
    }
    return sb.toString();
  }

  private static String getAppName() throws NamingException {
    String appName = "";
    try {
      String jndiName = "java:app/AppName";
      appName = (String) new InitialContext().lookup(jndiName);
    } catch (Throwable t) {
    }
    
    if (StringUtils.isBlank(appName))
      appName = "Pyramus";
    
    return appName;
  }
  
  private static Object findByClass(Class<?> cls) {
    try {
      String jndiName = "java:app/" + getAppName() + "/" + cls.getSimpleName();
      return new InitialContext().lookup(jndiName);
    } catch (NamingException e) {
      throw new PersistenceException(e);
    }
  }

  private static Set<String> toSet(String s) {
    return s == null ? Collections.emptySet() : Collections.singleton(s);
  }

}
