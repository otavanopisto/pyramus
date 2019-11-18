package fi.otavanopisto.pyramus.events;

public class OrganizationCreatedEvent {

  public OrganizationCreatedEvent(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  private Long id;
  private String name;

}
