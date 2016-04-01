package fi.otavanopisto.pyramus.webhooks;

public class Webhook {
  
  public Webhook(String url, String signature) {
    this.url = url;
    this.signature = signature;
  }

  public String getSignature() {
    return signature;
  }
  
  public String getUrl() {
    return url;
  }
  
  private String url;
  private String signature;
}
