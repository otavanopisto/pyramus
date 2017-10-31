package fi.otavanopisto.pyramus.events;

public class ApplicationModifiedByApplicantEvent {

  public ApplicationModifiedByApplicantEvent(Long entityId) {
    this.entityId = entityId;
  }
  
  public Long getEntityId() {
    return entityId;
  }
  
  private Long entityId;
}
