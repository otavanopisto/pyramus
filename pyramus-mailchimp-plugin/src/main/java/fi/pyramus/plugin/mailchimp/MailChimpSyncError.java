package fi.otavanopisto.pyramus.plugin.mailchimp;

public class MailChimpSyncError {
  
  public MailChimpSyncError(String error, String email) {
    this.email = email;
    this.error = error;
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getError() {
    return error;
  }
  
  private String error;
  private String email;
}