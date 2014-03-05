package fi.pyramus.plugin;

public class PageHookContext {
  
  public void setIncludeFtl(String includeFtl) {
    this.includeFtl = includeFtl;
  }
  
  public String getIncludeFtl() {
    return includeFtl;
  }
  
  private String includeFtl;
}
