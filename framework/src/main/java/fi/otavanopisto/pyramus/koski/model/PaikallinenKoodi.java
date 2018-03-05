package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaikallinenKoodi {

  public PaikallinenKoodi() {
  }
  
  public PaikallinenKoodi(String koodiarvo, Kuvaus nimi) {
    this.koodiarvo = koodiarvo;
    this.nimi = nimi;
  }
  
  public String getKoodistoUri() {
    return koodistoUri;
  }
  
  public void setKoodistoUri(String koodistoUri) {
    this.koodistoUri = koodistoUri;
  }
  
  public String getKoodiarvo() {
    return koodiarvo;
  }
  
  public Kuvaus getNimi() {
    return nimi;
  }
  
  public void setKoodiarvo(String koodiarvo) {
    this.koodiarvo = koodiarvo;
  }

  public void setNimi(Kuvaus nimi) {
    this.nimi = nimi;
  }

  private String koodiarvo;
  private Kuvaus nimi;
  private String koodistoUri;
}
