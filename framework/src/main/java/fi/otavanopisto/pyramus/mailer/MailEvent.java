package fi.otavanopisto.pyramus.mailer;

import java.util.List;
import java.util.Set;

public class MailEvent {

  public MailEvent(String jndiName, String mimeType, String from, Set<String> to, Set<String> cc, Set<String> bcc,
      String subject, String content, List<MailAttachment> attachments) {
    super();
    this.jndiName = jndiName;
    this.mimeType = mimeType;
    this.from = from;
    this.to = to;
    this.cc = cc;
    this.bcc = bcc;
    this.subject = subject;
    this.content = content;
    this.attachments = attachments;
  }

  public String getJndiName() {
    return jndiName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getFrom() {
    return from;
  }

  public Set<String> getTo() {
    return to;
  }

  public Set<String> getCc() {
    return cc;
  }

  public Set<String> getBcc() {
    return bcc;
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  public List<MailAttachment> getAttachments() {
    return attachments;
  }

  private String jndiName;
  private String mimeType;
  private String from;
  private Set<String> to;
  private Set<String> cc;
  private Set<String> bcc;
  private String subject;
  private String content;
  private List<MailAttachment> attachments;
}
