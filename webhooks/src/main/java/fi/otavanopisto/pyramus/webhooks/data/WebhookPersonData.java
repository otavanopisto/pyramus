package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookPersonData {

  public WebhookPersonData() {
    super();
  }

  public WebhookPersonData(Long personId) {
    super();
    this.personId = personId;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  private Long personId;
}
