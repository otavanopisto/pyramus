package fi.otavanopisto.pyramus.events;

public class OrganizationArchivedEvent {

  public OrganizationArchivedEvent(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  private Long id;

}
