package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaikallinenKoodi {

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
  
  private final String koodiarvo;
  private final Kuvaus nimi;
  private String koodistoUri;
}
