package fi.otavanopisto.pyramus.koski.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LahdejarjestelmaIdReturnVal {

  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("lähdejärjestelmä")
  public LahdejarjestelmaReturnVal getLahdejarjestelma() {
    return lahdejarjestelma;
  }

  public void setLahdejarjestelma(LahdejarjestelmaReturnVal lahdejarjestelma) {
    this.lahdejarjestelma = lahdejarjestelma;
  }

  private String id;
  private LahdejarjestelmaReturnVal lahdejarjestelma;
}
