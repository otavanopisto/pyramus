package fi.otavanopisto.pyramus.breadcrumbs;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/** A breadcrumb trail handler. */
public class BreadcrumbHandler {
  
  /** Clears the breadcrumb trail. */
  public void clear() {
    breadcrumbs.clear();
  }
  
  /** Returns <code>true</code> if the breadcrumb
   * trail contains <code>request</code>
   *  
   * @param request The request to look for.
   * @return <code>true</code> if the request was found.
   */
  public boolean contains(HttpServletRequest request) {
    return indexOf(getBreadcrumbUrl(request, true)) >= 0;
  }
  
  /** Process the given request and update the breadcrumb accordingly.
   * 
   * @param request The request to process.
   * @param breadcrumbable The target page of the request.
   */
  public void process(HttpServletRequest request, Breadcrumbable breadcrumbable) {
    String shortUrl = getBreadcrumbUrl(request, false);
    String completeUrl = getBreadcrumbUrl(request, true);
    String lastUrl = getSize() == 0 ? "" : breadcrumbs.get(breadcrumbs.size() - 1).getUrl();
    if (lastUrl.startsWith(shortUrl)) {
      pop();
      Breadcrumb breadcrumb = new Breadcrumb(completeUrl, breadcrumbable.getName(request.getLocale()));
      breadcrumbs.add(breadcrumb);
    }
    else {
      int index = indexOf(completeUrl);
      if (index == -1) {
        Breadcrumb breadcrumb = new Breadcrumb(completeUrl, breadcrumbable.getName(request.getLocale()));
        breadcrumbs.add(breadcrumb);
      }
      else {
        prune(index);
      }
    }
  }
  
  private int indexOf(String url) {
    for (int i = 0;  i < breadcrumbs.size(); i++) {
      if (breadcrumbs.get(i).getUrl().equals(url)) {
        return i;
      }
    }
    return -1;
  }
  
  private boolean isBreadcrumbParameter(String s) {
    return "resetbreadcrumb".equals(s);
  }
  
  /** Remove the last breadcrumb from the trail. */
  public void pop() {
    if (getSize() > 0) {
      breadcrumbs.remove(breadcrumbs.size() - 1);
    }
  }
  
  private void prune(int index) {
    while (breadcrumbs.size() - 1 > index) {
      breadcrumbs.remove(breadcrumbs.size() - 1);
    }
  }
  
  /** Returns the number of breadcrumbs in the trail.
   * 
   * @return the number of breadcrumbs in the trail.
   */
  public int getSize() {
    return breadcrumbs.size();
  }
  
  /** Returns all the breadcrumbs in the trail.
   * 
   * @return all the breadcrumbs in the trail.
   */
  public List<Breadcrumb> getBreadcrumbs() {
    return breadcrumbs;
  }
  
  /** Returns the URL of <code>request</code>, sanitized for breadcrumb use.
   * 
   * @param request The request to process.
   * @param includeRequestParams Set to <code>true</code> to include (non-breadcrumb)
   * parameters, <code>false</code> to include no parameters.
   * @return The sanitized URL.
   */
  public String getBreadcrumbUrl(HttpServletRequest request, boolean includeRequestParams) {
    StringBuilder sb = new StringBuilder();
    sb.append(request.getRequestURL());
    if (includeRequestParams) {
      boolean firstParam = true;
      Enumeration<String> params = request.getParameterNames();
      while (params.hasMoreElements()) {
        String param = params.nextElement();
        if (!isBreadcrumbParameter(param)) {
          sb.append(firstParam ? '?' : '&'); 
          sb.append(param);
          sb.append("=");
          sb.append(request.getParameter(param));
          firstParam = false;
        }
      }
    }
    return sb.toString();
  }
  
  private List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

}
