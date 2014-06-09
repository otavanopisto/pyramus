package fi.pyramus.plugin;

import java.util.Map;

import javax.servlet.jsp.PageContext;


public class PageHookContext {
  
  public PageHookContext(PageContext pageContext, Map<String, DynamicAttribute> dynamicAttributes) {
    this.pageContext = pageContext;
    this.dynamicAttributes = dynamicAttributes;
  }
  
  public void setIncludeFtl(String includeFtl) {
    this.includeFtl = includeFtl;
  }
  
  public String getIncludeFtl() {
    return includeFtl;
  }

  public DynamicAttribute getAttribute(String name) {
    return dynamicAttributes.get(name);
  }
  
  public PageContext getPageContext() {
    return pageContext;
  }

  private String includeFtl;
  private PageContext pageContext;
  private Map<String, DynamicAttribute> dynamicAttributes;
}
