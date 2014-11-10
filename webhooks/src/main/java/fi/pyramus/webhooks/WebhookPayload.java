package fi.pyramus.webhooks;

public abstract class WebhookPayload <T> {

  public WebhookPayload(WebhookType type, T data) {
    super();
    this.type = type;
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public WebhookType getType() {
    return type;
  }

  public void setType(WebhookType type) {
    this.type = type;
  }

  private WebhookType type;
  private T data;
}
