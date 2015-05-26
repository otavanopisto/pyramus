package fi.pyramus.events;

public class PersonArchivedEvent {

  public PersonArchivedEvent(Long personId) {
    super();
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }

  private Long personId;
}
