package fi.otavanopisto.pyramus.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

@Singleton
public class MailService {

  @Inject
  private Event<MailEvent> mailEvent;

  private static final Logger logger = Logger.getLogger(MailService.class.getName());

  public void sendMail(String jndiName, String mimeType, String from, List<String> to, List<String> cc, List<String> bcc, String subject,
      String content, List<MailAttachment> attachments) {
    mailEvent.fire(new MailEvent(jndiName, mimeType, from, to, cc, bcc, subject, content, attachments));
  }

  @Asynchronous
  @Lock(LockType.READ)
  public void onMailEvent(@Observes MailEvent event) {
    try {
      String jndiName = event.getJndiName();
      String mimeType = event.getMimeType();
      String from = event.getFrom();
      List<String> to = event.getTo();
      List<String> cc = event.getCc();
      List<String> bcc = event.getBcc();
      String subject = event.getSubject();
      String content = event.getContent();
      List<MailAttachment> attachments = event.getAttachments();

      Properties props = new Properties();

      InitialContext initialContext = new InitialContext(props);
      Session mailSession = (Session) initialContext.lookup(jndiName);

      MimeMessage message = new MimeMessage(mailSession);

      // Recipients

      if (!to.isEmpty()) {
        message.setRecipients(Message.RecipientType.TO, parseToAddressArray(to));
      }
      if (!cc.isEmpty()) {
        message.setRecipients(Message.RecipientType.CC, parseToAddressArray(cc));
      }
      if (!bcc.isEmpty()) {
        message.setRecipients(Message.RecipientType.BCC, parseToAddressArray(bcc));
      }

      // Sender

      if (from != null) {
        InternetAddress fromAddress = new InternetAddress(from);
        message.setFrom(fromAddress);
        message.setReplyTo(new InternetAddress[] { fromAddress });
      }

      // Content

      if (subject != null) {
        message.setSubject(subject);
      }

      message.setSentDate(new Date());

      // Attachments

      if (!attachments.isEmpty()) {
        Multipart multipart = new MimeMultipart();

        if (content != null) {
          MimeBodyPart contentBodyPart = new MimeBodyPart();
          contentBodyPart.setContent(content, mimeType.toString());
          multipart.addBodyPart(contentBodyPart);
        }

        for (MailAttachment attachment : attachments) {
          MimeBodyPart attachmentBodyPart = new MimeBodyPart();
          attachmentBodyPart.setContent(attachment.getContent(), attachment.getMimeType());
          attachmentBodyPart.setFileName(attachment.getFileName());
          multipart.addBodyPart(attachmentBodyPart);
        }

        message.setContent(multipart);
      }
      else {
        if (content != null) {
          message.setContent(content, mimeType.toString());
        }
      }

      // Sending

      Transport.send(message);
    }
    catch (Exception e) {
      logger.log(Level.SEVERE, "Error sending mail", e);
    }
  }

  private Address[] parseToAddressArray(List<String> list) throws AddressException {
    Address[] addresses = new Address[list.size()];

    for (int i = 0; i < list.size(); i++) {
      addresses[i] = new InternetAddress(list.get(i));
    }

    return addresses;
  }

}
