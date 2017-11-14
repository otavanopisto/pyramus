package fi.otavanopisto.pyramus.koski.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Kuvaus {

  public String getFi() {
    return fi;
  }
  
  public void setFi(String fi) {
    this.fi = fi;
  }
  
  public String getEn() {
    return en;
  }
  
  public void setEn(String en) {
    this.en = en;
  }
  
  public String getSv() {
    return sv;
  }

  public void setSv(String sv) {
    this.sv = sv;
  }
  
  private String fi;
  private String en;
  private String sv;
}
