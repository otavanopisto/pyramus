package fi.pyramus.plugin.mailchimp;

import java.util.ArrayList;
import java.util.List;

public class MailChimpSyncResult {
  
  public MailChimpSyncResult() {
    this.errors = new ArrayList<MailChimpSyncError>();
  }
  
  public int getAdded() {
    return added;
  }
  
  public void addAdded(int count) {
    this.added += count;
  }
  
  public int getRemoved() {
    return removed;
  }

  public void addRemoved(int count) {
    this.removed += count;
  }
  
  public int getUpdated() {
    return updated;
  }
  
  public void addUpdated(int count) {
    this.updated += count;
  }
  
  public List<MailChimpSyncError> getErrors() {
    return errors;
  }
  
  public void addError(String email, String error) {
    errors.add(new MailChimpSyncError(error, email));
  }
  
  private int added = 0;
  private int removed = 0;
  private int updated = 0;
  private List<MailChimpSyncError> errors;
}