package fi.otavanopisto.pyramus.mailer;

public interface MailErrorHandler {
  
  public void report(String subject, String errorMsg);

}
