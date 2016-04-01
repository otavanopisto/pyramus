package fi.otavanopisto.pyramus.events;

public class PersonUpdatedEvent {

  public PersonUpdatedEvent(Long personId) {
    super();
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }

  private Long personId;
}
