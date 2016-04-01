package fi.otavanopisto.pyramus.events;

public class PersonCreatedEvent {

  public PersonCreatedEvent(Long personId) {
    super();
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }

  private Long personId;
}
