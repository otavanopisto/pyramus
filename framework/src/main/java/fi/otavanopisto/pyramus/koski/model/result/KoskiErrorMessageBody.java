package fi.otavanopisto.pyramus.koski.model.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Kosken palauttamat virheviestit ovat muotoa:
 *   [{"key":"badRequest.validation.henkilötiedot.hetu","message":"Virheellinen tarkistusmerkki hetussa: xxxxx"}]
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KoskiErrorMessageBody {

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
  
  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }

  private String key;
  private String message;
}
