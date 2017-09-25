package fi.otavanopisto.pyramus.util;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class Mailer {

  public static final String PLAINTEXT = "text/plain; charset=UTF-8";
  public static final String HTML = "text/html; charset=UTF-8";
  
  public static final String JNDI_APPLICATION = "java:/mail/pyramus-haku";

  private static final Logger logger = Logger.getLogger(Mailer.class.getName());
  
  private final static Mailer instance = new Mailer();  
  
  public static Mailer getInstance() {
    return instance;
  }
  
  public void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content) {
    sendMail(jndiName, mimeType, from, Collections.singletonList(to), Collections.emptyList(), subject, content, Collections.emptyList());
  }

  public void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content, MailAttachment mailAttachment) {
    sendMail(jndiName, mimeType, from, Collections.singletonList(to), Collections.emptyList(), subject, content, Collections.singletonList(mailAttachment));
  }

  public void sendMail(String jndiName, String mimeType, String from, String to, String subject, String content, List<MailAttachment> mailAttachments) {
    sendMail(jndiName, mimeType, from, Collections.singletonList(to), Collections.emptyList(), subject, content, mailAttachments);
  }

  public void sendMail(String jndiName, String mimeType, String from, List<String> to, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, Collections.emptyList(), subject, content, Collections.emptyList());
  }

  public void sendMail(String jndiName, String mimeType, String from, List<String> to, String cc, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, Collections.singletonList(cc), subject, content, Collections.emptyList());
  }

  public void sendMail(String jndiName, String mimeType, String from, List<String> to, List<String> cc, String subject, String content) {
    sendMail(jndiName, mimeType, from, to, cc, subject, content, Collections.emptyList());
  }

  public void sendMail(String jndiName, String mimeType, String from, List<String> to, List<String> cc, String subject, String content, List<MailAttachment> mailAttachments) {
    MailService mailService = (MailService) findByClass(MailService.class);
    if (mailService == null) {
      logger.log(Level.SEVERE, "MailService not bound");
      return;
    }
    mailService.sendMail(jndiName, mimeType, from, to,  cc,  Collections.emptyList(), subject, content, mailAttachments);
  }

  private String getAppName() throws NamingException {
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
  
  private Object findByClass(Class<?> cls) {
    try {
      String jndiName = "java:app/" + getAppName() + "/" + cls.getSimpleName();
      return new InitialContext().lookup(jndiName);
    } catch (NamingException e) {
      throw new PersistenceException(e);
    }
  }

}
