package fi.pyramus.webhooks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@SessionScoped
@Stateful
public class WebhookSessionData {
  
  @PostConstruct
  public void init() {
    updatedStaffMemberIds = new ArrayList<>();
    updatedStudentIds = new ArrayList<>();
  }
  
  /* StaffMemberIds */
  
  public synchronized void addUpdatedStaffMemberId(Long updatedStaffMemberId) {
    if (!updatedStaffMemberIds.contains(updatedStaffMemberId)) {
      updatedStaffMemberIds.add(updatedStaffMemberId);
    }
  }
  
  public synchronized List<Long> retrieveUpdatedStaffMemberIds() {
    List<Long> result = new ArrayList<Long>(updatedStaffMemberIds);
    updatedStaffMemberIds.clear();
    return result;
  }

  public synchronized void clearUpdatedStaffMemberIds() {
    updatedStaffMemberIds.clear();
  }
  
  /* StudentIds */
  
  public synchronized void addUpdatedStudentId(Long updatedStudentId) {
    if (!updatedStudentIds.contains(updatedStudentId)) {
      updatedStudentIds.add(updatedStudentId);
    }
  }
  
  public synchronized List<Long> retrieveUpdatedStudentIds() {
    List<Long> result = new ArrayList<Long>(updatedStudentIds);
    updatedStudentIds.clear();
    return result;
  }

  public synchronized void clearUpdatedStudentIds() {
    updatedStudentIds.clear();
  }
  
  private List<Long> updatedStaffMemberIds;
  private List<Long> updatedStudentIds;
}
