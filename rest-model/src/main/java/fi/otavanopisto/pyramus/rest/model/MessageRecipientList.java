package fi.otavanopisto.pyramus.rest.model;

import java.util.List;

public class MessageRecipientList {
  
  public List<Long> getUserRecipients() {
    return userRecipientIds;
  }
  public void setUserRecipients(List<Long> userRecipientIds) {
    this.userRecipientIds = userRecipientIds;
  }
  public MessageRecipientList() {}
  
  public List<Long> getUserGroupRecipients() {
    return userGroupRecipientIds;
  }
  public void setUserGroupRecipients(List<Long> userGroupRecipientIds) {
    this.userGroupRecipientIds = userGroupRecipientIds;
  }

  public List<Long> getWorkspaceRecipients() {
    return workspaceRecipientIds;
  }
  public void setWorkspaceRecipients(List<Long> workspaceRecipientIds) {
    this.workspaceRecipientIds = workspaceRecipientIds;
  }

  private List<Long> userRecipientIds;
  private List<Long> userGroupRecipientIds;
  private List<Long> workspaceRecipientIds;
}
