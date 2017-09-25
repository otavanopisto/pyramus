package fi.otavanopisto.pyramus.util;

import java.util.List;

public class MailEvent {

  public MailEvent(String jndiName, String mimeType, String from, List<String> to, List<String> cc, List<String> bcc,
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

  public List<String> getTo() {
    return to;
  }

  public List<String> getCc() {
    return cc;
  }

  public List<String> getBcc() {
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
  private List<String> to;
  private List<String> cc;
  private List<String> bcc;
  private String subject;
  private String content;
  private List<MailAttachment> attachments;
}
