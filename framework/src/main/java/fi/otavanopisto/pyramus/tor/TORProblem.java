package fi.otavanopisto.pyramus.tor;

public class TORProblem {

  public TORProblem(TORProblemType type, String additionalInfo) {
    this.type = type;
    this.additionalInfo = additionalInfo;
  }
  
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public TORProblemType getType() {
    return type;
  }

  public void setType(TORProblemType type) {
    this.type = type;
  }

  private TORProblemType type;
  private String additionalInfo;
}
