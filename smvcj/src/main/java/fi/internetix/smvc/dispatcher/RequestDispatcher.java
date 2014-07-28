package fi.internetix.smvc.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestDispatcher {
  
  public boolean canHandle(HttpServletRequest request, HttpServletResponse response);

  public RequestDispatchContext getContext(HttpServletRequest request, HttpServletResponse response);
  
}
