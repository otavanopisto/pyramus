package fi.otavanopisto.pyramus.koski.model.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * TODO This could probably be replaced by custom jackson deserializer (StdSerializer) 
 * but due to generics in KoodistoViite it's a bit complicated to implement right now
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LahdejarjestelmaReturnVal {

  public String getKoodistoUri() {
    return koodistoUri;
  }

  public void setKoodistoUri(String koodistoUri) {
    this.koodistoUri = koodistoUri;
  }

  public String getKoodiarvo() {
    return koodiarvo;
  }

  public void setKoodiarvo(String koodiarvo) {
    this.koodiarvo = koodiarvo;
  }

  private String koodiarvo;
  private String koodistoUri;
}
