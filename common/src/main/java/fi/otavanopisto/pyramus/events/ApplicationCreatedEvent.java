package fi.otavanopisto.pyramus.events;

public class ApplicationCreatedEvent {

  public ApplicationCreatedEvent(Long entityId) {
    this.entityId = entityId;
  }
  
  public Long getEntityId() {
    return entityId;
  }
  
  private Long entityId;
}
