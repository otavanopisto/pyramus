package fi.otavanopisto.pyramus.rest.model;

public class StudentGroupUser {

  public StudentGroupUser() {
    super();
  }

  public StudentGroupUser(Long id, Long staffMemberId, boolean groupAdvisor, boolean studyAdvisor, boolean messageReceiver) {
    this();
    this.id = id;
    this.staffMemberId = staffMemberId;
    this.groupAdvisor = groupAdvisor;
    this.studyAdvisor = studyAdvisor;
    this.messageReceiver = messageReceiver;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getStaffMemberId() {
    return staffMemberId;
  }

  public void setStaffMemberId(Long staffMemberId) {
    this.staffMemberId = staffMemberId;
  }

  public boolean isGroupAdvisor() {
    return groupAdvisor;
  }

  public void setGroupAdvisor(boolean groupAdvisor) {
    this.groupAdvisor = groupAdvisor;
  }

  public boolean isStudyAdvisor() {
    return studyAdvisor;
  }

  public void setStudyAdvisor(boolean studyAdvisor) {
    this.studyAdvisor = studyAdvisor;
  }

  public boolean isMessageReceiver() {
    return messageReceiver;
  }

  public void setMessageReceiver(boolean messageReceiver) {
    this.messageReceiver = messageReceiver;
  }

  private Long id;
  private Long staffMemberId;
  private boolean groupAdvisor;
  private boolean studyAdvisor;
  private boolean messageReceiver;
}
